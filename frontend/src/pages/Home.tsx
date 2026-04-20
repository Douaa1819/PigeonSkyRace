import { Link } from 'react-router-dom';

export function Home() {
  return (
    <div className="animate-in stack" style={{ maxWidth: 640 }}>
      <h1 style={{ fontSize: '2rem', margin: 0 }}>Moroccan pigeon racing</h1>
      <p style={{ color: 'var(--muted)', margin: 0 }}>
        Sprint, middle-distance, and long-distance competitions — manage seasons, lofts, and results in one place.
      </p>
      <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
        <Link className="btn btn-primary" to="/login">
          Sign in
        </Link>
        <Link className="btn btn-ghost" to="/register">
          Create account
        </Link>
      </div>
    </div>
  );
}
