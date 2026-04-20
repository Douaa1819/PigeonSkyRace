import { useAuth } from '@/context/AuthContext';

export function BreederDashboard() {
  const { user } = useAuth();
  return (
    <div className="animate-in stack">
      <h1 style={{ margin: 0 }}>Breeder space</h1>
      <p style={{ color: 'var(--muted)', margin: 0 }}>
        Welcome, <strong>{user?.name}</strong>. Manage your lofts and pigeons, and follow rankings here.
      </p>
      <div className="grid grid-2" style={{ marginTop: '0.5rem' }}>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Lofts</h3>
          <p style={{ color: 'var(--muted)' }}>Register colombiers via POST /api/v1/colombiers.</p>
        </div>
        <div className="card">
          <h3 style={{ marginTop: 0 }}>Pigeons</h3>
          <p style={{ color: 'var(--muted)' }}>Add ringed birds via POST /api/v1/pigeons.</p>
        </div>
      </div>
    </div>
  );
}
