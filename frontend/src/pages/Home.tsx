import { motion, useScroll, useSpring, useTransform } from 'framer-motion';
import { useRef } from 'react';
import { Link } from 'react-router-dom';
import { useLocale } from '@/context/LocaleContext';
import { HeroFlightScene } from '@/components/three/HeroFlightScene';

export function Home() {
  const { t } = useLocale();
  const rootRef = useRef<HTMLDivElement | null>(null);

  const { scrollYProgress } = useScroll({ target: rootRef, offset: ['start start', 'end end'] });
  const smoothProgress = useSpring(scrollYProgress, { stiffness: 34, damping: 20, mass: 0.45 });
  const heroY = useTransform(smoothProgress, [0, 0.3], [0, -120]);
  const starsY = useTransform(smoothProgress, [0, 1], [0, -90]);
  const nebulaY = useTransform(smoothProgress, [0, 1], [0, -180]);
  const titleDepth = useTransform(smoothProgress, [0, 0.4], [1, 0.78]);

  return (
    <div className="home-obsidian" ref={rootRef}>
      <motion.div className="obsidian-stars" style={{ y: starsY }} />
      <motion.div className="obsidian-nebula-shift" style={{ y: nebulaY }} />

      <motion.section className="obsidian-hero" style={{ y: heroY }}>
        <motion.div className="obsidian-copy" style={{ scale: titleDepth }}>
          <p className="obsidian-kicker">P R E C I S I O N&nbsp;&nbsp;F L I G H T&nbsp;&nbsp;I N T E L L I G E N C E</p>
          <h1 className="obsidian-title">Dynamic Data Nebula for Moroccan Racing Pigeon Telemetry</h1>
          <p className="obsidian-sub">
            Real-time competition analytics, GPS motion intelligence and federation-grade race control in a single
            immersive data space.
          </p>
          <div className="obsidian-actions">
            <Link className="obsidian-link" to="/login">
              {t('home.cta.login')}
            </Link>
            <Link className="obsidian-link" to="/register">
              {t('home.cta.register')}
            </Link>
            <Link className="obsidian-link" to="/live">
              {t('home.cta.live')}
            </Link>
          </div>
        </motion.div>
        <HeroFlightScene />
      </motion.section>
    </div>
  );
}
