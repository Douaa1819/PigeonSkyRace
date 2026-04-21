import { useEffect, useMemo, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import type { CompetitionDto } from '@/api/types';
import type { LeaderboardRow } from '@/components/ranking/LeaderboardTable';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { Skeleton } from '@/components/ui/Skeleton';
import { LeaderboardTable } from '@/components/ranking/LeaderboardTable';
import { useApiGet } from '@/hooks/useApiGet';
import {
  useLiveCompetitionLeaderboard,
  type LiveConnectionStatus,
} from '@/hooks/useLiveCompetitionLeaderboard';
import { useRankMovement } from '@/hooks/useRankMovement';
import { competitionIsLive } from '@/utils/competitionLive';
import { resultatsToLeaderboardRows } from '@/utils/leaderboardRows';
import './live-race.css';

const DEV_DEMO_INTERVAL = 3200;

function statusLabel(status: LiveConnectionStatus) {
  if (status === 'websocket-connected') return 'WebSocket Connected';
  if (status === 'websocket-reconnecting') return 'Reconnecting';
  if (status === 'polling') return 'Fallback Polling';
  return 'Connecting';
}

function statusClass(status: LiveConnectionStatus) {
  if (status === 'websocket-connected') return 'live-race__status--ok';
  if (status === 'websocket-reconnecting' || status === 'websocket-connecting') return 'live-race__status--warn';
  return 'live-race__status--polling';
}

function simulateRows(prev: LeaderboardRow[]): LeaderboardRow[] {
  if (prev.length < 2) return prev;
  const next = [...prev];
  const limit = Math.min(7, next.length - 1);
  const i = Math.max(0, Math.floor(Math.random() * limit));
  [next[i], next[i + 1]] = [next[i + 1], next[i]];

  return next.map((row, index) => {
    const rank = index + 1;
    const speedBase = row.speed ?? Math.max(80, 150 - rank * 2.4);
    const pointsBase = row.points ?? Math.max(10, 100 - rank * 3.25);
    return {
      ...row,
      rank,
      speed: Math.max(1, speedBase + (Math.random() * 1.7 - 0.7)),
      points: Math.max(1, pointsBase + (Math.random() * 1.4 - 0.6)),
    };
  });
}

export function LiveRace() {
  const [params, setParams] = useSearchParams();
  const cParam = params.get('c') ?? '';
  const isDev = import.meta.env.DEV;
  const [demoMode, setDemoMode] = useState(isDev);
  const [demoRows, setDemoRows] = useState<LeaderboardRow[]>([]);

  const { data: competitions, loading: loadingComps, error: errComps } =
    useApiGet<CompetitionDto[]>('/v1/competions');

  useEffect(() => {
    if (!competitions?.length || cParam) return;
    setParams({ c: competitions[0].id }, { replace: true });
  }, [competitions, cParam, setParams]);

  const active = useMemo(() => competitions?.find((x) => x.id === cParam), [competitions, cParam]);
  const isWindowLive = competitionIsLive(active);

  const { data: rawResults, loading: loadingRes, error: errRes, transport, connectionStatus } =
    useLiveCompetitionLeaderboard(cParam || null);

  const rows = useMemo(() => resultatsToLeaderboardRows(rawResults ?? []), [rawResults]);
  const displayRows = demoMode ? demoRows : rows;
  const podium = displayRows.slice(0, 3);

  useEffect(() => {
    setDemoRows(rows);
  }, [rows, cParam]);

  useEffect(() => {
    if (!isDev || !demoMode || rows.length < 2) return;
    const id = window.setInterval(() => {
      setDemoRows((curr) => simulateRows(curr.length ? curr : rows));
    }, DEV_DEMO_INTERVAL);
    return () => window.clearInterval(id);
  }, [demoMode, isDev, rows]);

  const movementByKey = useRankMovement(
    displayRows.map((r) => ({ pigeonId: r.pigeonId, id: r.id, rank: r.rank })),
    `${cParam}-${demoMode ? 'demo' : 'live'}`
  );

  const loading = loadingComps || (cParam ? loadingRes : false);
  const error = errComps || errRes;

  return (
    <div className="stack live-race" style={{ gap: '1.5rem' }}>
      <div className="live-race__head">
        <SectionTitle
          eyebrow="Broadcast"
          title="Live race view"
          subtitle="Premium realtime leaderboard with animated rank movement, podium spotlight, and resilient transport fallback."
        />
        <div className="live-race__badge-row">
          <span className="live-race__pulse" aria-live="polite">
            <span className="live-race__dot" />
            {isWindowLive ? 'LIVE / ONLINE' : 'ONLINE'}
          </span>
          <span className={`live-race__status ${statusClass(connectionStatus)}`}>
            {statusLabel(connectionStatus)}
          </span>
        </div>
      </div>

      <GlassCard className="results-toolbar">
        <div className="results-toolbar__inner">
          <label className="label" htmlFor="live-competition">
            Competition
          </label>
          {loadingComps ? (
            <Skeleton height={44} />
          ) : (
            <select
              id="live-competition"
              className="input select-like"
              value={cParam}
              onChange={(e) => setParams(e.target.value ? { c: e.target.value } : {})}
            >
              {!competitions?.length ? (
                <option value="">No competitions</option>
              ) : (
                competitions.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.nom}
                  </option>
                ))
              )}
            </select>
          )}
          <p className="results-toolbar__meta muted" style={{ margin: 0 }}>
            {transport === 'websocket' ? 'WebSocket live stream' : 'Fallback polling'} ·{' '}
            {isWindowLive ? 'Inside scheduled race window' : 'Outside race window'}
          </p>
          {isDev && (
            <button
              type="button"
              className="btn btn-ghost live-race__demo-btn"
              onClick={() => setDemoMode((x) => !x)}
            >
              {demoMode ? 'Disable DEV demo' : 'Enable DEV demo'}
            </button>
          )}
        </div>
      </GlassCard>

      {error && <div className="error-banner">{error}</div>}

      {loading && (
        <div className="stack live-race__loading" style={{ gap: '0.65rem' }}>
          <Skeleton height={118} />
          <Skeleton height={48} />
          <Skeleton height={48} />
          <Skeleton height={48} />
        </div>
      )}

      {!loading && !error && cParam && rawResults && (
        <>
          {displayRows.length === 0 ? (
            <GlassCard style={{ padding: '1.5rem' }}>
              <p className="muted" style={{ margin: 0 }}>
                No results for this competition yet.
              </p>
            </GlassCard>
          ) : (
            <>
              <div className="live-race__podium">
                {podium.map((r) => (
                  <GlassCard key={r.id} className={`live-race__podium-card live-race__podium-card--${r.rank}`}>
                    <p className="live-race__podium-rank">#{r.rank}</p>
                    <p className="live-race__podium-name">{r.title}</p>
                    <p className="live-race__podium-meta">{r.subtitle || 'Elite competitor'}</p>
                  </GlassCard>
                ))}
              </div>
              <LeaderboardTable rows={displayRows} liveMode maxRows={10} movementByKey={movementByKey} />
            </>
          )}
          <p className="muted" style={{ margin: 0 }}>
            <Link to={`/results?c=${encodeURIComponent(cParam)}`}>Open full leaderboard</Link>
            {' · '}
            <Link to="/competitions">All competitions</Link>
          </p>
        </>
      )}
    </div>
  );
}
