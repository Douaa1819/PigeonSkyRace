import { Bird } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useLocale } from '@/context/LocaleContext';
import { FooterPreferences } from '@/components/FooterPreferences';

/**
 * "Floating lists" footer — no separate surface; text sits on page --bg.
 */
export function SpatialFooter() {
  const { t } = useLocale();

  return (
    <footer className="spatial-footer" role="contentinfo" aria-label="Site">
      <div className="spatial-footer__air" aria-hidden />
      <div className="spatial-footer__grid">
        <div className="spatial-footer__col">
          <div className="spatial-footer__brand">
            <Bird className="spatial-footer__icon" size={20} strokeWidth={1.25} aria-hidden />
            <span>PigeonSkyRace</span>
          </div>
          <p className="spatial-footer__line">{t('footer.spatial.copyright')}</p>
        </div>

        <div className="spatial-footer__col">
          <h3 className="spatial-footer__k">{t('footer.spatial.titleExplore')}</h3>
          <ul className="spatial-footer__list">
            <li>
              <Link to="/competitions">{t('nav.competitions')}</Link>
            </li>
            <li>
              <Link to="/breeder">{t('nav.breeder')}</Link>
            </li>
            <li>
              <Link to="/results">{t('nav.rankings')}</Link>
            </li>
          </ul>
        </div>

        <div className="spatial-footer__col">
          <h3 className="spatial-footer__k">{t('footer.spatial.titleSpecs')}</h3>
          <ul className="spatial-footer__list spatial-footer__list--mono">
            <li>{t('footer.spatial.spec1')}</li>
            <li>{t('footer.spatial.spec2')}</li>
            <li>{t('footer.spatial.spec3')}</li>
          </ul>
        </div>

        <div className="spatial-footer__col">
          <h3 className="spatial-footer__k">{t('footer.spatial.titlePrefs')}</h3>
          <div className="spatial-footer__toggles">
            <FooterPreferences />
          </div>
        </div>
      </div>
    </footer>
  );
}
