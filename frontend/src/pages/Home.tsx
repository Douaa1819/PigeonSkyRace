import { motion, useInView, useScroll, useSpring, useTransform } from 'framer-motion';
import { useMemo, useRef } from 'react';
import { Link } from 'react-router-dom';
import { useLocale } from '@/context/LocaleContext';
import { HeroFlightScene } from '@/components/three/HeroFlightScene';

function useCounter(target: number, active: boolean) {
  const m = useSpring(0, { stiffness: 42, damping: 18 });
  const rounded = useTransform(m, (latest) => latest.toFixed(0));
  if (active) m.set(target);
  return rounded;
}

export function Home() {
  const { t } = useLocale();
  const rootRef = useRef<HTMLDivElement | null>(null);
  const featureRef = useRef<HTMLDivElement | null>(null);
  const statsRef = useRef<HTMLDivElement | null>(null);
  const statsInView = useInView(statsRef, { once: true, margin: '-100px' });

  const { scrollYProgress } = useScroll({ target: rootRef, offset: ['start start', 'end end'] });
  const heroY = useTransform(scrollYProgress, [0, 0.2], [0, -80]);
  const cloudY = useTransform(scrollYProgress, [0, 1], [0, -180]);
  const titleOpacity = useTransform(scrollYProgress, [0, 0.12], [1, 0.35]);

  const speedCount = useCounter(142, statsInView);
  const loftCount = useCounter(87, statsInView);
  const raceCount = useCounter(123, statsInView);

  const features = useMemo(
    () => [
      {
        title: 'Precision GPS Tracking',
        desc: 'Live telemetry from release point to loft arrival, with route and speed intelligence.',
      },
      {
        title: 'Federation Competition Engine',
        desc: 'Manage speed, demi-fond and fond events with strict rules and controlled race flows.',
      },
      {
        title: 'Athlete-Grade Pigeon Profiles',
        desc: 'Ring identity, loft ownership, performance history and ranking momentum in one workspace.',
      },
    ],
    []
  );

  return (
    <div className="home-premium" ref={rootRef}>
      <motion.div className="home-parallax-layer" style={{ y: cloudY }} />

      <motion.section className="home-premium-hero" style={{ y: heroY, opacity: titleOpacity }}>
        <div className="hero-copy">
          <span className="home-kicker">Precision flight intelligence system</span>
          <h1 className="home-hero-title">Moroccan Federation Racing Pigeon Analytics Platform</h1>
          <p className="home-hero-sub">
            A premium competition engine combining GPS precision, telemetry-level performance metrics, and live
            ranking intelligence for breeders, organizers, and federation teams.
          </p>
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
        </div>
        <HeroFlightScene />
      </motion.section>

      <section className="home-section" ref={featureRef}>
        <header className="section-head">
          <span className="section-kicker">Core features</span>
          <h2>Built like a sports telemetry stack</h2>
        </header>
        <div className="feature-grid">
          {features.map((item, idx) => (
            <motion.article
              key={item.title}
              className="feature-card"
              initial={{ opacity: 0, y: 26, filter: 'blur(10px)' }}
              whileInView={{ opacity: 1, y: 0, filter: 'blur(0px)' }}
              viewport={{ once: true, margin: '-120px' }}
              transition={{ duration: 0.55, delay: idx * 0.09, ease: [0.22, 1, 0.36, 1] }}
              whileHover={{ rotateX: -3, rotateY: 3, y: -8 }}
            >
              <h3>{item.title}</h3>
              <p>{item.desc}</p>
            </motion.article>
          ))}
        </div>
      </section>

      <section className="home-section">
        <header className="section-head">
          <span className="section-kicker">How it works</span>
          <h2>Federation competition lifecycle</h2>
        </header>
        <div className="timeline">
          {[
            'Breeders register lofts and ringed pigeons in the active season.',
            'Organizers create races with launch GPS, schedule, and race type.',
            'Race results are uploaded, ranked, and published for consultation and export.',
          ].map((step, i) => (
            <motion.div
              key={step}
              className="timeline-step"
              initial={{ opacity: 0, x: -20 }}
              whileInView={{ opacity: 1, x: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.45, delay: i * 0.08 }}
            >
              <span>{`0${i + 1}`}</span>
              <p>{step}</p>
            </motion.div>
          ))}
        </div>
      </section>

      <section className="home-section stats-section" ref={statsRef}>
        <header className="section-head">
          <span className="section-kicker">Live telemetry</span>
          <h2>Real-time competitive intelligence</h2>
        </header>
        <div className="stats-grid">
          <div className="stat-card">
            <strong><motion.span>{speedCount}</motion.span>.38 km/h</strong>
            <p>Peak speed capture</p>
          </div>
          <div className="stat-card">
            <strong><motion.span>{raceCount}</motion.span></strong>
            <p>Season race sessions tracked</p>
          </div>
          <div className="stat-card">
            <strong><motion.span>{loftCount}</motion.span></strong>
            <p>Active loft telemetry nodes</p>
          </div>
        </div>
      </section>

      <section className="home-section">
        <header className="section-head">
          <span className="section-kicker">Leaderboard experience</span>
          <h2>Dynamic podium and rank movement</h2>
        </header>
        <div className="podium-preview">
          {[{ r: '2', c: 'silver' }, { r: '1', c: 'gold' }, { r: '3', c: 'bronze' }].map((p, i) => (
            <motion.div
              key={p.r}
              className={`podium-card podium-${p.c}`}
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.08, type: 'spring', stiffness: 120, damping: 16 }}
            >
              <span>Rank {p.r}</span>
            </motion.div>
          ))}
        </div>
      </section>

      <section className="home-final-cta">
        <motion.div
          initial={{ opacity: 0, scale: 0.96, filter: 'blur(6px)' }}
          whileInView={{ opacity: 1, scale: 1, filter: 'blur(0px)' }}
          viewport={{ once: true }}
          transition={{ duration: 0.55 }}
        >
          <h2>Launch your next federation race with telemetry precision.</h2>
          <p>Move from manual race coordination to a live, measurable, and auditable competition platform.</p>
          <div className="home-hero__actions">
            <Link className="btn btn-primary" to="/register">
              Create Federation Workspace
            </Link>
          </div>
        </motion.div>
      </section>
    </div>
  );
}
