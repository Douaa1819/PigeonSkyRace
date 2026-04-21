import { AnimatePresence, motion } from 'framer-motion';
import { useState } from 'react';
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { FloatingOrbs } from '@/components/ambient/FloatingOrbs';
import { SkyBackground } from '@/components/ambient/SkyBackground';
import { useAuth } from '@/context/AuthContext';
import { useLocale } from '@/context/LocaleContext';
import { useTheme } from '@/context/ThemeContext';

export function Layout() {
  const { user, logout } = useAuth();
  const { theme, toggle } = useTheme();
  const { locale, toggleLocale, t } = useLocale();
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
          {menuOpen ? t('nav.close') : t('nav.menu')}
        </button>
        <nav className={`nav-links ${menuOpen ? 'nav-links--open' : ''}`}>
          {user && (
            <>
              {(user.role === 'ADMIN' || user.role === 'ORGANIZER') && (
                <NavLink to="/organizer" onClick={closeMenu}>
                  {t('nav.organizer')}
                </NavLink>
              )}
              {user.role === 'BREEDER' && (
                <NavLink to="/breeder" onClick={closeMenu}>
                  {t('nav.breeder')}
                </NavLink>
              )}
              <NavLink to="/competitions" onClick={closeMenu}>
                {t('nav.competitions')}
              </NavLink>
              <NavLink to="/live" onClick={closeMenu}>
                {t('nav.live')}
              </NavLink>
              <NavLink to="/pigeons" onClick={closeMenu}>
                {t('nav.pigeons')}
              </NavLink>
              <NavLink to="/results" onClick={closeMenu}>
                {t('nav.rankings')}
              </NavLink>
            </>
          )}
          <button type="button" className="btn btn-ghost" onClick={toggleLocale} aria-label="Toggle language">
            {locale === 'en' ? t('lang.fr') : t('lang.en')}
          </button>
          <button type="button" className="btn btn-ghost" onClick={toggle} aria-label="Toggle theme">
            {theme === 'dark' ? t('theme.light') : t('theme.dark')}
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
              {t('nav.signOut')}
            </button>
          ) : (
            <>
              <NavLink to="/login" onClick={closeMenu}>
                {t('nav.login')}
              </NavLink>
              <NavLink to="/register" onClick={closeMenu}>
                {t('nav.register')}
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
