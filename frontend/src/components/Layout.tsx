import { Link, Outlet } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import { useTheme } from '@/context/ThemeContext';

export function Layout() {
  const { user, logout } = useAuth();
  const { theme, toggle } = useTheme();

  return (
    <>
      <header className="nav">
        <Link to="/" style={{ fontWeight: 700, fontSize: '1.1rem', color: 'var(--text)' }}>
          PigeonSkyRace
        </Link>
        <nav className="nav-links">
          {user && (
            <>
              {(user.role === 'ADMIN' || user.role === 'ORGANIZER') && (
                <Link to="/organizer">Organizer</Link>
              )}
              {user.role === 'BREEDER' && <Link to="/breeder">Breeder</Link>}
              <Link to="/competitions">Competitions</Link>
              <Link to="/pigeons">Pigeons</Link>
              <Link to="/results">Results</Link>
            </>
          )}
          <button type="button" className="btn btn-ghost" onClick={toggle} aria-label="Toggle theme">
            {theme === 'dark' ? 'Light' : 'Dark'}
          </button>
          {user ? (
            <button type="button" className="btn btn-ghost" onClick={logout}>
              Sign out
            </button>
          ) : (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register">Register</Link>
            </>
          )}
        </nav>
      </header>
      <main className="page">
        <Outlet />
      </main>
    </>
  );
}
