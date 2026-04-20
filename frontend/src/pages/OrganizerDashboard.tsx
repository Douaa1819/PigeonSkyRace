import { useAuth } from '@/context/AuthContext';

export function OrganizerDashboard() {
  const { user } = useAuth();
  return (
    <div className="animate-in stack">
      <h1 style={{ margin: 0 }}>Organizer dashboard</h1>
      <p style={{ color: 'var(--muted)', margin: 0 }}>
        Signed in as <strong>{user?.email}</strong> ({user?.role}). Use this area to manage seasons, competitions, and results.
      </p>
      <div className="grid grid-2" style={{ marginTop: '0.5rem' }}>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Seasons</h3>
          <p style={{ color: 'var(--muted)' }}>Create and list seasons (API: POST /api/v1/saisons).</p>
        </div>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Competitions</h3>
          <p style={{ color: 'var(--muted)' }}>Attach competitions to a season (API: POST /api/v1/competions).</p>
        </div>
      </div>
    </div>
  );
}
