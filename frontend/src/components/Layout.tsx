import { AnimatePresence, motion } from 'framer-motion';
import { Link, Outlet, useLocation } from 'react-router-dom';
import { FloatingOrbs } from '@/components/ambient/FloatingOrbs';
import { SkyBackground } from '@/components/ambient/SkyBackground';
import { useAuth } from '@/context/AuthContext';
import { useTheme } from '@/context/ThemeContext';

export function Layout() {
  const { user, logout } = useAuth();
  const { theme, toggle } = useTheme();
  const location = useLocation();

  return (
    <div className="app-root">
      <SkyBackground />
      <FloatingOrbs />
      <header className="nav">
        <Link to="/" className="nav-brand">
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
              <Link to="/results">Rankings</Link>
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
      <main className="app-main">
        <AnimatePresence mode="wait">
          <motion.div
            key={location.pathname}
            className="page"
            initial={{ opacity: 0, y: 14 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            transition={{ duration: 0.28, ease: [0.22, 1, 0.36, 1] }}
          >
            <Outlet />
          </motion.div>
        </AnimatePresence>
      </main>
    </div>
  );
}
