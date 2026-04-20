import { Link } from 'react-router-dom';
import type { CompetitionDto } from '@/api/types';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { Skeleton } from '@/components/ui/Skeleton';
import { useApiGet } from '@/hooks/useApiGet';

export function Competitions() {
  const { data: items, loading, error } = useApiGet<CompetitionDto[]>('/v1/competions');

  return (
    <div className="stack" style={{ gap: '1.5rem' }}>
      <SectionTitle
        eyebrow="Season calendar"
        title="Competitions"
        subtitle="Official races — open live rankings for any event."
      />

      {error && <div className="error-banner">{error}</div>}

      <GlassCard style={{ padding: 0, overflow: 'hidden' }}>
        {loading ? (
          <div className="stack" style={{ padding: '1rem', gap: '0.65rem' }}>
            <Skeleton height={40} />
            <Skeleton height={40} />
            <Skeleton height={40} />
          </div>
        ) : (
          <table className="competitions-table">
            <thead>
              <tr>
                <th scope="col">Race</th>
                <th scope="col">Pigeons</th>
                <th scope="col" className="competitions-table__actions">
                  Standings
                </th>
              </tr>
            </thead>
            <tbody>
              {items?.map((c) => (
                <tr key={c.id}>
                  <td>
                    <span className="competitions-table__name">{c.nom}</span>
                    <span className="competitions-table__id muted">{c.id}</span>
                  </td>
                  <td className="competitions-table__num">{c.nbPigeons ?? '—'}</td>
                  <td className="competitions-table__actions">
                    <Link className="btn btn-primary competitions-table__cta" to={`/results?c=${encodeURIComponent(c.id)}`}>
                      Rankings
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        {!loading && (!items || items.length === 0) && (
          <p className="muted" style={{ padding: '1.25rem', margin: 0 }}>
            No competitions yet.
          </p>
        )}
      </GlassCard>
    </div>
  );
}
