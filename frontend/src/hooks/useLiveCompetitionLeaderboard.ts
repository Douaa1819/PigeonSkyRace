import { Client, type IMessage } from '@stomp/stompjs';
import { useEffect, useMemo, useRef, useState } from 'react';
import SockJS from 'sockjs-client';
import type { ResultatDto } from '@/api/types';
import { getStoredToken } from '@/api/client';
import { useApiGet, usePollingApiGet } from '@/hooks/useApiGet';

const POLL_MS = 5000;
export type LiveConnectionStatus =
  | 'websocket-connecting'
  | 'websocket-connected'
  | 'websocket-reconnecting'
  | 'polling';

/**
 * REST bootstrap while WebSocket is active; STOMP pushes override.
 * If connect/auth fails, falls back to polling-only (no duplicate bootstrap).
 */
export function useLiveCompetitionLeaderboard(competitionId: string | null) {
  const [transport, setTransport] = useState<'websocket' | 'polling'>('websocket');
  const [stompData, setStompData] = useState<ResultatDto[] | null>(null);
  const [connectionStatus, setConnectionStatus] = useState<LiveConnectionStatus>('websocket-connecting');
  const closingIntentionallyRef = useRef(false);

  useEffect(() => {
    setTransport('websocket');
    setStompData(null);
    setConnectionStatus('websocket-connecting');
    closingIntentionallyRef.current = false;
  }, [competitionId]);

  const path = competitionId ? `/v1/resultats/${encodeURIComponent(competitionId)}` : '';

  const bootstrap = useApiGet<ResultatDto[]>(path, !!competitionId && transport === 'websocket');
  const poll = usePollingApiGet<ResultatDto[]>(path, POLL_MS, !!competitionId && transport === 'polling');

  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    if (!competitionId || transport !== 'websocket') {
      if (clientRef.current) {
        closingIntentionallyRef.current = true;
        void clientRef.current.deactivate();
        clientRef.current = null;
      }
      return;
    }

    const token = getStoredToken();
    if (!token) {
      setTransport('polling');
      setConnectionStatus('polling');
      return;
    }
    setConnectionStatus('websocket-connecting');
    closingIntentionallyRef.current = false;

    const client = new Client({
      webSocketFactory: () => new SockJS('/ws') as unknown as WebSocket,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      reconnectDelay: 4000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      debug: () => undefined,
      onConnect: () => {
        setConnectionStatus('websocket-connected');
        client.subscribe(`/topic/competitions/${competitionId}/leaderboard`, (message: IMessage) => {
          try {
            const parsed = JSON.parse(message.body) as ResultatDto[];
            setStompData(parsed);
          } catch {
            /* ignore */
          }
        });
      },
      onStompError: () => {
        setTransport('polling');
        setConnectionStatus('polling');
      },
      onWebSocketError: () => {
        setTransport('polling');
        setConnectionStatus('polling');
      },
      onWebSocketClose: () => {
        if (!closingIntentionallyRef.current) {
          setConnectionStatus('websocket-reconnecting');
        }
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
      closingIntentionallyRef.current = true;
      void client.deactivate();
      clientRef.current = null;
    };
  }, [competitionId, transport]);

  const data = useMemo(() => {
    if (transport === 'websocket') {
      return stompData ?? bootstrap.data;
    }
    return poll.data;
  }, [transport, stompData, bootstrap.data, poll.data]);

  const loading =
    !!competitionId &&
    (transport === 'websocket' ? bootstrap.loading && data == null : poll.loading && data == null);

  const error = transport === 'websocket' ? bootstrap.error : poll.error;

  return {
    data,
    loading,
    error,
    transport,
    connectionStatus,
  };
}
