import { FooterPreferences } from '@/components/FooterPreferences';
import { useLocale } from '@/context/LocaleContext';

/** Preferences strip for routes without the home spatial grid (moved out of header). */
export function LayoutPreferencesFooter() {
  const { t } = useLocale();
  return (
    <div className="layout-pref-footer" role="contentinfo" aria-label={t('footer.spatial.titlePrefs')}>
      <div className="layout-pref-footer__air" aria-hidden />
      <div className="layout-pref-footer__inner">
        <FooterPreferences />
      </div>
    </div>
  );
}
