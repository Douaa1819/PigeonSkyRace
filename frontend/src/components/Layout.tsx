import { AnimatePresence, motion } from 'framer-motion';
import { useState } from 'react';
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { FloatingOrbs } from '@/components/ambient/FloatingOrbs';
import { SkyBackground } from '@/components/ambient/SkyBackground';
import { useAuth } from '@/context/AuthContext';
import { useTheme } from '@/context/ThemeContext';

export function Layout() {
  const { user, logout } = useAuth();
  const { theme, toggle } = useTheme();
  const location = useLocation();
  const [menuOpen, setMenuOpen] = useState(false);

  function closeMenu() {
    setMenuOpen(false);
  }

  return (
    <div className="app-root">
      <SkyBackground />
      <FloatingOrbs />
      <header className="nav">
        <Link to="/" className="nav-brand">
          PigeonSkyRace
        </Link>
        <button
          type="button"
          className="nav-mobile-toggle btn btn-ghost"
          aria-label="Toggle navigation"
          onClick={() => setMenuOpen((x) => !x)}
        >
          {menuOpen ? 'Close' : 'Menu'}
        </button>
        <nav className={`nav-links ${menuOpen ? 'nav-links--open' : ''}`}>
          {user && (
            <>
              {(user.role === 'ADMIN' || user.role === 'ORGANIZER') && (
                <NavLink to="/organizer" onClick={closeMenu}>
                  Organizer
                </NavLink>
              )}
              {user.role === 'BREEDER' && (
                <NavLink to="/breeder" onClick={closeMenu}>
                  Breeder
                </NavLink>
              )}
              <NavLink to="/competitions" onClick={closeMenu}>
                Competitions
              </NavLink>
              <NavLink to="/live" onClick={closeMenu}>
                Live
              </NavLink>
              <NavLink to="/pigeons" onClick={closeMenu}>
                Pigeons
              </NavLink>
              <NavLink to="/results" onClick={closeMenu}>
                Rankings
              </NavLink>
            </>
          )}
          <button type="button" className="btn btn-ghost" onClick={toggle} aria-label="Toggle theme">
            {theme === 'dark' ? 'Light' : 'Dark'}
          </button>
          {user ? (
            <button
              type="button"
              className="btn btn-ghost"
              onClick={() => {
                closeMenu();
                logout();
              }}
            >
              Sign out
            </button>
          ) : (
            <>
              <NavLink to="/login" onClick={closeMenu}>
                Login
              </NavLink>
              <NavLink to="/register" onClick={closeMenu}>
                Register
              </NavLink>
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
