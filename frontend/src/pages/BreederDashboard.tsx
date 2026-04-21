import { useMemo } from 'react';
import { Link } from 'react-router-dom';
import type { ColombierDto } from '@/api/types';
import { PigeonCard } from '@/components/pigeon/PigeonCard';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { SkeletonCard } from '@/components/ui/Skeleton';
import { useAuth } from '@/context/AuthContext';
import { useApiGet } from '@/hooks/useApiGet';

export function BreederDashboard() {
  const { user } = useAuth();
  const { data, loading, error } = useApiGet<ColombierDto[]>('/v1/colombiers');

  const flat = useMemo(() => {
    if (!data) return [];
    return data.flatMap((c) =>
      (c.pigeons ?? []).map((p) => ({
        ...p,
        loftName: c.nomColombier,
      }))
    );
  }, [data]);

  return (
    <div className="stack" style={{ gap: '1.5rem' }}>
      <SectionTitle
        eyebrow="Breeder HQ"
        title={`Welcome, ${user?.name ?? 'Champion'}`}
        subtitle="Your lofts and racing athletes — performance and entries in one premium workspace."
      />

      {error && <div className="error-banner">{error}</div>}

      <div className="grid grid-2">
        <GlassCard className="home-feature" hoverLift={false}>
          <h3>Lofts</h3>
          <p>
            Register and manage colombiers via the API, then attach ringed birds to each loft.
          </p>
          <Link className="btn btn-primary" to="/pigeons" style={{ marginTop: '0.75rem', width: 'fit-content' }}>
            View roster
          </Link>
        </GlassCard>
        <GlassCard className="home-feature" hoverLift={false}>
          <h3>Standings</h3>
          <p>Follow official points and speed rankings as races close.</p>
          <Link className="btn btn-primary" to="/results" style={{ marginTop: '0.75rem', width: 'fit-content' }}>
            Open rankings
          </Link>
        </GlassCard>
      </div>

      <h2 style={{ margin: '0.25rem 0 0', fontSize: '1.15rem' }}>Your athletes</h2>

      {loading && (
        <div className="premium-grid premium-grid--3">
          {Array.from({ length: 4 }).map((_, i) => (
            <SkeletonCard key={i} />
          ))}
        </div>
      )}

      {!loading && (
        <div className="premium-grid premium-grid--3">
          {flat.map((p, index) => (
            <PigeonCard
              key={p.id}
              index={index}
              pigeon={{
                id: p.id,
                numeroBague: p.numeroBague,
                imageUrl: p.imageUrl,
                sexe: p.sexe,
                age: p.age,
                couleur: p.couleur,
                loftName: p.loftName,
              }}
            />
          ))}
        </div>
      )}

      {!loading && flat.length === 0 && (
        <p className="muted" style={{ margin: 0 }}>
          No pigeons in your lofts yet. Add colombiers and birds via the API.
        </p>
      )}
    </div>
  );
}
