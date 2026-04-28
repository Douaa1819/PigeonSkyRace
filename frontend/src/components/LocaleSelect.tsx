import { ChevronDown } from 'lucide-react';
import { useLocale } from '@/context/LocaleContext';
import type { Locale } from '@/i18n/translations';

/**
 * Themed <select> — solid bg, high contrast, no native chevron (custom).
 */
export function LocaleSelect({ id, className = '' }: { id?: string; className?: string }) {
  const { locale, setLocale, t } = useLocale();

  return (
    <div className={`locale-select-ui ${className}`.trim()}>
      <select
        id={id}
        className="locale-select-ui__input"
        value={locale}
        onChange={(e) => setLocale(e.target.value as Locale)}
        aria-label={t('footer.spatial.langLabel')}
      >
        <option value="fr">{t('lang.fr')}</option>
        <option value="en">{t('lang.en')}</option>
        <option value="ar">{t('lang.ar')}</option>
      </select>
      <ChevronDown className="locale-select-ui__chev" size={14} strokeWidth={1.75} aria-hidden />
    </div>
  );
}
