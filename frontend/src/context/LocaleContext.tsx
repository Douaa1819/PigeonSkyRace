import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
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
    if (saved === 'en' || saved === 'ar' || saved === 'fr') return saved;
    return 'fr';
  });

  const setLocale = (next: Locale) => {
    setLocaleRaw(next);
    localStorage.setItem(STORAGE_KEY, next);
  };

  const toggleLocale = () => {
    if (locale === 'fr') {
      setLocale('en');
      return;
    }
    if (locale === 'en') {
      setLocale('ar');
      return;
    }
    setLocale('fr');
  };

  useEffect(() => {
    const root = document.documentElement;
    const isRtl = locale === 'ar';
    root.lang = locale;
    root.dir = isRtl ? 'rtl' : 'ltr';
    root.classList.toggle('rtl', isRtl);
    root.classList.toggle('ltr', !isRtl);
  }, [locale]);

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
