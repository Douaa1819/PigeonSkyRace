import { useEffect, useMemo } from 'react';
import { useSearchParams } from 'react-router-dom';
import type { CompetitionDto, ResultatDto } from '@/api/types';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { Skeleton } from '@/components/ui/Skeleton';
import { LeaderboardTable } from '@/components/ranking/LeaderboardTable';
import { useApiGet } from '@/hooks/useApiGet';

function sortByPoints(list: ResultatDto[]) {
  return [...list].sort((a, b) => {
    const pa = a.points ?? Number.NEGATIVE_INFINITY;
    const pb = b.points ?? Number.NEGATIVE_INFINITY;
    return pb - pa;
  });
}

export function Results() {
  const [params, setParams] = useSearchParams();
  const cParam = params.get('c') ?? '';

  const { data: competitions, loading: loadingComps, error: errComps } =
    useApiGet<CompetitionDto[]>('/v1/competions');

  useEffect(() => {
    if (!competitions?.length || cParam) return;
    setParams({ c: competitions[0].id }, { replace: true });
  }, [competitions, cParam, setParams]);

  const { data: rawResults, loading: loadingRes, error: errRes } = useApiGet<ResultatDto[]>(
    `/v1/resultats/${encodeURIComponent(cParam)}`,
    !!cParam
  );

  const activeName = useMemo(
    () => competitions?.find((x) => x.id === cParam)?.nom,
    [competitions, cParam]
  );

  const rows = useMemo(() => {
    const sorted = sortByPoints(rawResults ?? []);
    return sorted.map((r, i) => ({
      id: r.id,
      rank: i + 1,
      title: `Athlete #${i + 1}`,
      points: r.points,
      speed: r.vitesse,
      distance: r.distance,
      arrival: r.dateArrivee,
    }));
  }, [rawResults]);

  const loading = loadingComps || (cParam ? loadingRes : false);
  const error = errComps || errRes;

  return (
    <div className="stack" style={{ gap: '1.5rem' }}>
      <SectionTitle
        eyebrow="Live standings"
        title="Rankings & results"
        subtitle={
          <>
            Points-ranked leaderboard for the selected race. Backend returns timing rows; pigeon ring
            linkage can be layered when the API exposes it.
          </>
        }
      />

      <GlassCard className="results-toolbar">
        <div className="results-toolbar__inner">
          <label className="label" htmlFor="competition">
            Competition
          </label>
          {loadingComps ? (
            <Skeleton height={44} />
          ) : (
            <select
              id="competition"
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
          {activeName && <p className="results-toolbar__meta muted">Selected: {activeName}</p>}
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
            <LeaderboardTable rows={rows} />
          )}
        </>
      )}
    </div>
  );
}
