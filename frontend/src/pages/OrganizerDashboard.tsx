import { Link } from 'react-router-dom';
import type { CompetitionDto, SaisonDto } from '@/api/types';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { Skeleton } from '@/components/ui/Skeleton';
import { useAuth } from '@/context/AuthContext';
import { useApiGet } from '@/hooks/useApiGet';

export function OrganizerDashboard() {
  const { user } = useAuth();
  const saisons = useApiGet<SaisonDto[]>('/v1/saisons');
  const comps = useApiGet<CompetitionDto[]>('/v1/competions');

  const loading = saisons.loading || comps.loading;
  const error = saisons.error || comps.error;

  return (
    <div className="stack" style={{ gap: '1.5rem' }}>
      <SectionTitle
        eyebrow="Control center"
        title="Organizer dashboard"
        subtitle={
          <>
            Signed in as <strong>{user?.email}</strong> ({user?.role}). Manage seasons, races, and published
            results from here.
          </>
        }
      />

      {error && <div className="error-banner">{error}</div>}

      <div className="grid grid-3">
        <GlassCard className="home-feature" hoverLift={false}>
          <h3>Seasons</h3>
          {loading ? (
            <Skeleton height={36} />
          ) : (
            <p style={{ fontSize: '2rem', fontWeight: 800, margin: '0.25rem 0' }}>{saisons.data?.length ?? 0}</p>
          )}
          <p className="muted" style={{ margin: 0 }}>
            Total seasons on record
          </p>
        </GlassCard>
        <GlassCard className="home-feature" hoverLift={false}>
          <h3>Competitions</h3>
          {loading ? (
            <Skeleton height={36} />
          ) : (
            <p style={{ fontSize: '2rem', fontWeight: 800, margin: '0.25rem 0' }}>{comps.data?.length ?? 0}</p>
          )}
          <p className="muted" style={{ margin: 0 }}>
            Races available to breeders
          </p>
        </GlassCard>
        <GlassCard className="home-feature" hoverLift={false}>
          <h3>Live board</h3>
          <p className="muted" style={{ margin: '0 0 0.75rem' }}>Open the animated leaderboard for any race.</p>
          <Link className="btn btn-primary" to="/results">
            Rankings
          </Link>
        </GlassCard>
      </div>

      <GlassCard style={{ padding: '1.25rem' }} hoverLift={false}>
        <h3 style={{ marginTop: 0 }}>Race operations</h3>
        <p className="muted" style={{ margin: '0 0 0.75rem' }}>
          Create seasons and competitions through the REST API, then monitor standings as results are posted.
        </p>
        <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
          <Link className="btn btn-ghost" to="/competitions">
            All competitions
          </Link>
          <Link className="btn btn-ghost" to="/pigeons">
            Pigeon registry
          </Link>
        </div>
      </GlassCard>
    </div>
  );
}
