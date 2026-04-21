import { AnimatePresence, motion } from 'framer-motion';
import { useState } from 'react';
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { Bird, Globe2, MoonStar, SunMedium } from 'lucide-react';
import { FloatingOrbs } from '@/components/ambient/FloatingOrbs';
import { SkyBackground } from '@/components/ambient/SkyBackground';
import { useAuth } from '@/context/AuthContext';
import { useLocale } from '@/context/LocaleContext';
import { useTheme } from '@/context/ThemeContext';

export function Layout() {
  const { user, logout } = useAuth();
  const { theme, toggle } = useTheme();
  const { locale, setLocale, t } = useLocale();
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
          <Bird size={17} />
          <span>PigeonSkyRace</span>
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
              {user.role === 'ADMIN' && (
                <NavLink to="/admin" onClick={closeMenu}>
                  {t('nav.admin')}
                </NavLink>
              )}
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
          <label className="nav-lang">
            <Globe2 size={15} />
            <select
              className="input nav-lang__select"
              value={locale}
              onChange={(e) => setLocale(e.target.value as 'fr' | 'en' | 'ar')}
              aria-label="Language"
            >
              <option value="fr">{t('lang.fr')}</option>
              <option value="en">{t('lang.en')}</option>
              <option value="ar">{t('lang.ar')}</option>
            </select>
          </label>
          <button type="button" className="btn btn-ghost" onClick={toggle} aria-label="Toggle theme">
            {theme === 'dark' ? <SunMedium size={15} /> : <MoonStar size={15} />}
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
