import { Client, type IMessage } from '@stomp/stompjs';
import { useEffect, useMemo, useRef, useState } from 'react';
import SockJS from 'sockjs-client';
import type { ResultatDto } from '@/api/types';
import { getStoredToken } from '@/api/client';
import { useApiGet, usePollingApiGet } from '@/hooks/useApiGet';

const POLL_MS = 5000;

/**
 * REST bootstrap while WebSocket is active; STOMP pushes override.
 * If connect/auth fails, falls back to polling-only (no duplicate bootstrap).
 */
export function useLiveCompetitionLeaderboard(competitionId: string | null) {
  const [transport, setTransport] = useState<'websocket' | 'polling'>('websocket');
  const [stompData, setStompData] = useState<ResultatDto[] | null>(null);

  useEffect(() => {
    setTransport('websocket');
    setStompData(null);
  }, [competitionId]);

  const path = competitionId ? `/v1/resultats/${encodeURIComponent(competitionId)}` : '';

  const bootstrap = useApiGet<ResultatDto[]>(path, !!competitionId && transport === 'websocket');
  const poll = usePollingApiGet<ResultatDto[]>(path, POLL_MS, !!competitionId && transport === 'polling');

  const clientRef = useRef<Client | null>(null);

  useEffect(() => {
    if (!competitionId || transport !== 'websocket') {
      if (clientRef.current) {
        void clientRef.current.deactivate();
        clientRef.current = null;
      }
      return;
    }

    const token = getStoredToken();
    if (!token) {
      setTransport('polling');
      return;
    }

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
      },
      onWebSocketError: () => {
        setTransport('polling');
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
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
  };
}
