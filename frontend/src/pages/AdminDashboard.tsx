import type { ColombierDto, CompetitionDto, PigeonDto } from '@/api/types';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { Skeleton } from '@/components/ui/Skeleton';
import { useApiGet } from '@/hooks/useApiGet';
import { UsersRound, Bird, Trophy } from 'lucide-react';

export function AdminDashboard() {
  const pigeons = useApiGet<PigeonDto[]>('/v1/pigeons');
  const comps = useApiGet<CompetitionDto[]>('/v1/competions');
  const lofts = useApiGet<ColombierDto[]>('/v1/colombiers');
  const loading = pigeons.loading || comps.loading || lofts.loading;
  const error = pigeons.error || comps.error || lofts.error;

  return (
    <div className="stack" style={{ gap: '1.5rem' }}>
      <SectionTitle
        eyebrow="Admin Center"
        title="Federation system overview"
        subtitle="Global command view for platform activity, role distribution, competition coverage, and federation operations."
      />

      {error && <div className="error-banner">{error}</div>}

      <div className="grid grid-3">
        <GlassCard className="home-feature" hoverLift={false}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <UsersRound size={16} /> Lofts
          </h3>
          {loading ? (
            <Skeleton height={36} />
          ) : (
            <p style={{ fontSize: '2rem', fontWeight: 800, margin: '0.25rem 0' }}>{lofts.data?.length ?? 0}</p>
          )}
        </GlassCard>
        <GlassCard className="home-feature" hoverLift={false}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Bird size={16} /> Pigeons
          </h3>
          {loading ? (
            <Skeleton height={36} />
          ) : (
            <p style={{ fontSize: '2rem', fontWeight: 800, margin: '0.25rem 0' }}>{pigeons.data?.length ?? 0}</p>
          )}
        </GlassCard>
        <GlassCard className="home-feature" hoverLift={false}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Trophy size={16} /> Competitions
          </h3>
          {loading ? (
            <Skeleton height={36} />
          ) : (
            <p style={{ fontSize: '2rem', fontWeight: 800, margin: '0.25rem 0' }}>{comps.data?.length ?? 0}</p>
          )}
        </GlassCard>
      </div>
    </div>
  );
}
