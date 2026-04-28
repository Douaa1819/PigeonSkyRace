import { useState } from 'react';
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { Bird } from 'lucide-react';
import { FloatingOrbs } from '@/components/ambient/FloatingOrbs';
import { LayoutPreferencesFooter } from '@/components/LayoutPreferencesFooter';
import { SkyBackground } from '@/components/ambient/SkyBackground';
import { useAuth } from '@/context/AuthContext';
import { useLocale } from '@/context/LocaleContext';

export function Layout() {
  const { user, logout } = useAuth();
  const { t } = useLocale();
  const location = useLocation();
  const isHome = location.pathname === '/';
  const [menuOpen, setMenuOpen] = useState(false);
  const showBurger = !isHome || Boolean(user);

  function closeMenu() {
    setMenuOpen(false);
  }

  return (
    <div className="app-root">
      {!isHome && <SkyBackground />}
      {!isHome && <FloatingOrbs />}
      <header
        className={`nav ${isHome ? 'nav--spatial nav--obsidian' : ''} ${
          isHome ? 'nav--home-cinematic' : ''
        } ${isHome && !user ? 'nav--home-guest' : ''}`}
      >
        <Link to="/" className="nav-brand">
          <Bird size={17} />
          <span>PigeonSkyRace</span>
        </Link>

        {showBurger && (
          <button
            type="button"
            className="nav-burger"
            aria-label={menuOpen ? t('nav.close') : t('nav.menu')}
            aria-expanded={menuOpen}
            onClick={() => setMenuOpen((x) => !x)}
          >
            <span className="nav-burger__line" />
            <span className="nav-burger__line" />
            <span className="nav-burger__line" />
          </button>
        )}

        {isHome && !user ? (
          <div className="nav--home__auth" role="group" aria-label="Account access">
            <NavLink to="/login" onClick={closeMenu} className="nav--home__link" end>
              {t('nav.login')}
            </NavLink>
            <span className="nav--home__dot" aria-hidden>
              ·
            </span>
            <NavLink to="/register" onClick={closeMenu} className="nav--home__link" end>
              {t('nav.register')}
            </NavLink>
          </div>
        ) : null}

        {(!isHome || (isHome && user)) && (
          <nav className={`nav-links ${menuOpen ? 'nav-links--open' : ''}`} aria-label="Main">
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
                {(user.role === 'ADMIN' || user.role === 'ORGANIZER') && (
                  <NavLink to="/competitions" onClick={closeMenu}>
                    {t('nav.competitions')}
                  </NavLink>
                )}
                {(user.role === 'ADMIN' || user.role === 'ORGANIZER') && (
                  <NavLink to="/live" onClick={closeMenu}>
                    {t('nav.live')}
                  </NavLink>
                )}
                {user.role === 'BREEDER' && (
                  <NavLink to="/pigeons" onClick={closeMenu}>
                    {t('nav.pigeons')}
                  </NavLink>
                )}
                <NavLink to="/results" onClick={closeMenu}>
                  {t('nav.rankings')}
                </NavLink>
              </>
            )}
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
              !isHome && (
                <>
                  <NavLink to="/login" onClick={closeMenu}>
                    {t('nav.login')}
                  </NavLink>
                  <NavLink to="/register" onClick={closeMenu}>
                    {t('nav.register')}
                  </NavLink>
                </>
              )
            )}
          </nav>
        )}
      </header>
      <main className="app-main">
        <div key={location.pathname} className={`page ${isHome ? 'page--obsidian' : ''}`}>
          <Outlet />
        </div>
      </main>
      {!isHome && <LayoutPreferencesFooter />}
    </div>
  );
}
