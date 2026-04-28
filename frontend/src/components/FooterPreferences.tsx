import { MoonStar, SunMedium } from 'lucide-react';
import { useTheme } from '@/context/ThemeContext';
import { useLocale } from '@/context/LocaleContext';
import { LocaleSelect } from '@/components/LocaleSelect';

/** Ghost toggles: thin border, no fill — language + Clair/Sombre. */
export function FooterPreferences() {
  const { theme, toggle } = useTheme();
  const { t } = useLocale();

  return (
    <div className="footer-prefs" role="group" aria-label={t('footer.spatial.titlePrefs')}>
      <LocaleSelect id="footer-pref-locale" className="footer-prefs__select" />
      <button type="button" className="footer-prefs__theme" onClick={toggle} aria-label="Toggle theme">
        {theme === 'dark' ? <SunMedium size={14} strokeWidth={1.4} className="footer-prefs__ico" /> : <MoonStar size={14} strokeWidth={1.4} className="footer-prefs__ico" />}
        <span className="footer-prefs__theme-txt">{theme === 'dark' ? t('theme.light') : t('theme.dark')}</span>
      </button>
    </div>
  );
}
