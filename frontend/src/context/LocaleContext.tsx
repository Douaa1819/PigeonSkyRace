import { createContext, useContext, useMemo, useState, type ReactNode } from 'react';
import type { Locale } from '@/i18n/translations';
import { translations } from '@/i18n/translations';

type LocaleContextValue = {
  locale: Locale;
  setLocale: (locale: Locale) => void;
  toggleLocale: () => void;
  t: (key: string) => string;
};

const STORAGE_KEY = 'psr_locale';
const LocaleContext = createContext<LocaleContextValue | null>(null);

export function LocaleProvider({ children }: { children: ReactNode }) {
  const [locale, setLocaleRaw] = useState<Locale>(() => {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved === 'fr' ? 'fr' : 'en';
  });

  const setLocale = (next: Locale) => {
    setLocaleRaw(next);
    localStorage.setItem(STORAGE_KEY, next);
  };

  const toggleLocale = () => setLocale(locale === 'en' ? 'fr' : 'en');

  const value = useMemo(
    () => ({
      locale,
      setLocale,
      toggleLocale,
      t: (key: string) => translations[locale][key] ?? translations.en[key] ?? key,
    }),
    [locale]
  );

  return <LocaleContext.Provider value={value}>{children}</LocaleContext.Provider>;
}

export function useLocale() {
  const ctx = useContext(LocaleContext);
  if (!ctx) throw new Error('useLocale must be used within LocaleProvider');
  return ctx;
}
