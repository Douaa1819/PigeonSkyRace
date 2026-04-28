import { ChevronDown } from 'lucide-react';
import { useLocale } from '@/context/LocaleContext';
import type { Locale } from '@/i18n/translations';

/**
 * Themed <select> — solid bg, high contrast, no native chevron (custom).
 */
export function LocaleSelect({ id, className = '' }: { id?: string; className?: string }) {
  const { locale, setLocale, t } = useLocale();
  const OPTIONS: ReadonlyArray<{ value: Locale; label: string }> = [
    { value: 'fr', label: 'Français' },
    { value: 'en', label: 'English' },
    { value: 'ar', label: 'العربية' },
  ];

  return (
    <div className={`locale-select-ui ${className}`.trim()}>
      <select
        id={id}
        className="locale-select-ui__input"
        value={locale}
        onChange={(e) => {
          const next = e.target.value as Locale;
          setLocale(next);
        }}
        aria-label={t('footer.spatial.langLabel')}
      >
        {OPTIONS.map((opt) => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
      <ChevronDown className="locale-select-ui__chev" size={14} strokeWidth={1.75} aria-hidden />
    </div>
  );
}
