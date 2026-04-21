import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { GlassCard } from '@/components/ui/GlassCard';
import { useLocale } from '@/context/LocaleContext';
import { heroImageForId } from '@/components/pigeon/pigeonImagery';

const fade = {
  initial: { opacity: 0, y: 12 },
  animate: { opacity: 1, y: 0 },
};

export function Home() {
  const { t } = useLocale();

  return (
    <div className="home stack" style={{ gap: '1.85rem' }}>
      <motion.section className="home-hero" {...fade} transition={{ duration: 0.42, ease: [0.22, 1, 0.36, 1] }}>
        <span className="home-kicker">{t('home.kicker')}</span>
        <h1 className="home-hero-title">{t('home.title')}</h1>
        <p className="home-hero-sub">{t('home.subtitle')}</p>
        <div className="home-hero__actions">
          <Link className="btn btn-primary" to="/login">
            {t('home.cta.login')}
          </Link>
          <Link className="btn btn-ghost" to="/register">
            {t('home.cta.register')}
          </Link>
          <Link className="btn btn-ghost" to="/live">
            {t('home.cta.live')}
          </Link>
        </div>
      </motion.section>

      <section className="home-fly-grid">
        {[heroImageForId('fly-a'), heroImageForId('fly-b'), heroImageForId('fly-c')].map((img, idx) => (
          <motion.div
            key={img}
            className="home-fly-grid__item"
            initial={{ opacity: 0, y: 14 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.38, delay: idx * 0.07 }}
          >
            <img src={img} alt="" loading="lazy" />
          </motion.div>
        ))}
      </section>

      <motion.section className="home-features" initial="hidden" animate="show" variants={{ hidden: {}, show: { transition: { staggerChildren: 0.06 } } }}>
        {[
          {
            t: t('home.features.live.title'),
            d: t('home.features.live.desc'),
          },
          {
            t: t('home.features.ops.title'),
            d: t('home.features.ops.desc'),
          },
          {
            t: t('home.features.athlete.title'),
            d: t('home.features.athlete.desc'),
          },
        ].map((x) => (
          <motion.div key={x.t} variants={{ hidden: { opacity: 0, y: 10 }, show: { opacity: 1, y: 0 } }}>
            <GlassCard className="home-feature" hoverLift>
              <h3>{x.t}</h3>
              <p>{x.d}</p>
            </GlassCard>
          </motion.div>
        ))}
      </motion.section>

      <section className="home-grid home-grid--2">
        <GlassCard className="home-section-card" hoverLift={false}>
          <h3>{t('home.how.title')}</h3>
          <ol className="home-list">
            <li>{t('home.how.1')}</li>
            <li>{t('home.how.2')}</li>
            <li>{t('home.how.3')}</li>
          </ol>
        </GlassCard>
        <GlassCard className="home-section-card" hoverLift={false}>
          <h3>{t('home.benefits.title')}</h3>
          <ul className="home-list">
            <li>{t('home.benefits.1')}</li>
            <li>{t('home.benefits.2')}</li>
            <li>{t('home.benefits.3')}</li>
          </ul>
        </GlassCard>
      </section>

      <section className="home-grid home-grid--2">
        <GlassCard className="home-preview" hoverLift={false}>
          <h3>{t('home.preview.title')}</h3>
          <p className="muted">{t('home.preview.subtitle')}</p>
          <div className="home-preview__mock">
            <div className="home-preview__bar" />
            <div className="home-preview__body">
              <span />
              <span />
              <span />
            </div>
          </div>
        </GlassCard>
        <GlassCard className="home-preview" hoverLift={false}>
          <h3>{t('home.trust.title')}</h3>
          <p className="muted">{t('home.trust.subtitle')}</p>
          <div className="home-preview__image">
            <img src={heroImageForId('competition-visual')} alt="" loading="lazy" />
          </div>
        </GlassCard>
      </section>

      <footer className="home-footer muted">{t('home.footer')}</footer>
    </div>
  );
}
