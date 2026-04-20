import { useMemo } from 'react';
import type { ColombierDto, PigeonDto } from '@/api/types';
import { PigeonCard } from '@/components/pigeon/PigeonCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { SkeletonCard } from '@/components/ui/Skeleton';
import { useApiGet } from '@/hooks/useApiGet';

export function Pigeons() {
  const lofts = useApiGet<ColombierDto[]>('/v1/colombiers');
  const pigeons = useApiGet<PigeonDto[]>('/v1/pigeons');

  const loftByPigeonId = useMemo(() => {
    const m = new Map<string, string>();
    lofts.data?.forEach((c) => {
      c.pigeons?.forEach((p) => m.set(p.id, c.nomColombier));
    });
    return m;
  }, [lofts.data]);

  const loading = lofts.loading || pigeons.loading;
  const error = pigeons.error ?? lofts.error;

  const list = pigeons.data ?? [];

  return (
    <div className="stack" style={{ gap: '1.5rem' }}>
      <SectionTitle
        eyebrow="Roster"
        title="Pigeon athletes"
        subtitle="Every bird is a competitor — ring ID, loft, and profile stats from your live API."
      />

      {error && <div className="error-banner">{error}</div>}
      {lofts.error && !pigeons.error && (
        <div className="error-banner" style={{ borderColor: 'rgba(251, 191, 36, 0.35)', background: 'rgba(251, 191, 36, 0.08)' }}>
          Loft names unavailable: {lofts.error}
        </div>
      )}

      {loading && (
        <div className="premium-grid premium-grid--3">
          {Array.from({ length: 6 }).map((_, i) => (
            <SkeletonCard key={i} />
          ))}
        </div>
      )}

      {!loading && (
        <div className="premium-grid premium-grid--3">
          {list.map((p, index) => (
            <PigeonCard
              key={p.id}
              index={index}
              pigeon={{
                id: p.id,
                numeroBague: p.numeroBague,
                sexe: p.sexe,
                age: p.age,
                couleur: p.couleur,
                loftName: loftByPigeonId.get(p.id),
              }}
            />
          ))}
        </div>
      )}

      {!loading && list.length === 0 && (
        <p className="muted" style={{ margin: 0 }}>
          No pigeons registered yet.
        </p>
      )}
    </div>
  );
}
