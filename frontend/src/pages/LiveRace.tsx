import { useEffect, useMemo } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import type { CompetitionDto, ResultatDto } from '@/api/types';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { Skeleton } from '@/components/ui/Skeleton';
import { LeaderboardTable } from '@/components/ranking/LeaderboardTable';
import { useApiGet, usePollingApiGet } from '@/hooks/useApiGet';
import { useRankMovement } from '@/hooks/useRankMovement';
import { competitionIsLive } from '@/utils/competitionLive';
import { resultatsToLeaderboardRows } from '@/utils/leaderboardRows';
import './live-race.css';

const POLL_MS = 4000;

export function LiveRace() {
  const [params, setParams] = useSearchParams();
  const cParam = params.get('c') ?? '';

  const { data: competitions, loading: loadingComps, error: errComps } =
    useApiGet<CompetitionDto[]>('/v1/competions');

  useEffect(() => {
    if (!competitions?.length || cParam) return;
    setParams({ c: competitions[0].id }, { replace: true });
  }, [competitions, cParam, setParams]);

  const active = useMemo(() => competitions?.find((x) => x.id === cParam), [competitions, cParam]);
  const isWindowLive = competitionIsLive(active);

  const { data: rawResults, loading: loadingRes, error: errRes } = usePollingApiGet<ResultatDto[]>(
    `/v1/resultats/${encodeURIComponent(cParam)}`,
    POLL_MS,
    !!cParam
  );

  const rows = useMemo(() => resultatsToLeaderboardRows(rawResults ?? []), [rawResults]);

  const movementByKey = useRankMovement(
    rows.map((r) => ({ pigeonId: r.pigeonId, id: r.id, rank: r.rank })),
    cParam
  );

  const loading = loadingComps || (cParam ? loadingRes : false);
  const error = errComps || errRes;

  return (
    <div className="stack live-race" style={{ gap: '1.5rem' }}>
      <div className="live-race__head">
        <SectionTitle
          eyebrow="Broadcast"
          title="Live race view"
          subtitle="Top 10 refresh every few seconds with rank movement hints. Full table stays on Rankings."
        />
        {isWindowLive && (
          <span className="live-race__pulse" aria-live="polite">
            <span className="live-race__dot" /> Live window
          </span>
        )}
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
            Polling · {POLL_MS / 1000}s · {isWindowLive ? 'Inside scheduled race window' : 'Outside race window'}
          </p>
        </div>
      </GlassCard>

      {error && <div className="error-banner">{error}</div>}

      {loading && (
        <div className="stack" style={{ gap: '0.65rem' }}>
          <Skeleton height={48} />
          <Skeleton height={48} />
          <Skeleton height={48} />
        </div>
      )}

      {!loading && !error && cParam && rawResults && (
        <>
          {rows.length === 0 ? (
            <GlassCard style={{ padding: '1.5rem' }}>
              <p className="muted" style={{ margin: 0 }}>
                No results for this competition yet.
              </p>
            </GlassCard>
          ) : (
            <LeaderboardTable rows={rows} liveMode maxRows={10} movementByKey={movementByKey} />
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
