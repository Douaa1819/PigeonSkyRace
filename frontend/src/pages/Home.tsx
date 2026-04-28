import { motion, useInView, useScroll, useSpring, useTransform } from 'framer-motion';
import { useRef, type ReactNode } from 'react';
import { Link } from 'react-router-dom';
import { SpatialFooter } from '@/components/SpatialFooter';
import { useLocale } from '@/context/LocaleContext';
import { useTheme } from '@/context/ThemeContext';

const HERO_IMAGE = '/Gemini_Generated_Image_6ryj736ryj736ryj.png';

/* Golden path segments — lengths for stroke-dashoffset draw */
const MAP_L1 = Math.hypot(500 - 330, 220 - 260);
const MAP_L2 = Math.hypot(330 - 286, 470 - 260);

function Reveal({ children, className = '' }: { children: ReactNode; className?: string }) {
  const ref = useRef<HTMLDivElement | null>(null);
  const isInView = useInView(ref, { once: true, amount: 0.12, margin: '0px 0px -8% 0px' });
  return (
    <motion.div
      ref={ref}
      className={className}
      initial={{ opacity: 0, y: 32 }}
      animate={isInView ? { opacity: 1, y: 0 } : { opacity: 0, y: 32 }}
      transition={{ duration: 0.65, ease: [0.22, 1, 0.36, 1] }}
    >
      {children}
    </motion.div>
  );
}

function LineStagger({ className }: { className?: string }) {
  const { t } = useLocale();
  const ref = useRef<HTMLHeadingElement | null>(null);
  const inView = useInView(ref, { once: true, amount: 0.3, margin: '0px 0px -8% 0px' });
  const lines = [t('home.landing.mission.l1'), t('home.landing.mission.l2'), t('home.landing.mission.l3')] as const;
  const full = t('home.landing.mission.aria');

  return (
    <h2 ref={ref} className={className} aria-label={full}>
      {lines.map((line, i) => (
        <span key={i} className="line-stagger__row">
          <motion.span
            className="line-stagger__inner"
            initial={{ opacity: 0, y: 32 }}
            animate={inView ? { opacity: 1, y: 0 } : { opacity: 0, y: 32 }}
            transition={{ duration: 0.58, delay: i * 0.1, ease: [0.22, 1, 0.36, 1] }}
          >
            {line}
          </motion.span>
        </span>
      ))}
    </h2>
  );
}

export function Home() {
  const { t } = useLocale();
  const { theme } = useTheme();
  const isDark = theme === 'dark';
  const rootRef = useRef<HTMLDivElement | null>(null);
  const mechanicsRef = useRef<HTMLElement | null>(null);
  const mapRef = useRef<HTMLDivElement | null>(null);

  const { scrollYProgress } = useScroll({ target: rootRef, offset: ['start start', 'end end'] });
  const { scrollYProgress: mapPathProgress } = useScroll({
    target: mapRef,
    offset: ['start 0.92', 'end 0.12'],
  });
  const smoothProgress = useSpring(scrollYProgress, { stiffness: 34, damping: 20, mass: 0.45 });
  const bgY = useTransform(smoothProgress, [0, 1], [0, -100]);
  const mapY = useTransform(smoothProgress, [0, 1], [0, -28]);

  const photoScale = useTransform(smoothProgress, [0, 1], [1, 1.05]);
  const baseImageOpacity = isDark ? 0.15 : 0.05;
  const photoLayerOpacity = useTransform(smoothProgress, (p) => {
    const tFade = Math.max(0, Math.min(1, (p - 0.1) / 0.3));
    return baseImageOpacity * (1 - tFade);
  });

  const routeDraw = useTransform(mapPathProgress, [0, 1], [0, 1], { clamp: true });
  const routeOffset1 = useTransform(routeDraw, (d) => (1 - d) * MAP_L1);
  const routeOffset2 = useTransform(routeDraw, (d) => (1 - d) * MAP_L2);

  return (
    <div className="home-obsidian" ref={rootRef}>
      <motion.div className="race-progress" style={{ scaleX: smoothProgress }} />
      <motion.div
        key={theme}
        className="obsidian-photo"
        style={{
          scale: photoScale,
          y: bgY,
          opacity: photoLayerOpacity,
          backgroundImage: `url('${HERO_IMAGE}')`,
        }}
      />
      <div className="obsidian-photo__veil" aria-hidden />

      <motion.div ref={mapRef} className="morocco-map-overlay" style={{ y: mapY }}>
        <svg viewBox="0 0 900 700" className="morocco-map-svg" aria-hidden>
          <motion.path
            d="M276 90 L350 120 L452 104 L548 130 L640 206 L666 302 L624 384 L646 474 L572 556 L486 600 L420 586 L360 608 L308 570 L260 512 L236 430 L220 346 L238 276 L264 236 L250 164 Z"
            className="morocco-outline"
            initial={{ pathLength: 0 }}
            whileInView={{ pathLength: 1 }}
            viewport={{ once: true, amount: 0.5 }}
            transition={{ duration: 1.4, ease: 'easeOut' }}
          />
          <motion.line
            x1="330"
            y1="260"
            x2="500"
            y2="220"
            className="map-route map-route--drawn"
            style={{
              strokeDasharray: `${MAP_L1} ${MAP_L1}`,
              strokeDashoffset: routeOffset1,
            }}
          />
          <motion.line
            x1="330"
            y1="260"
            x2="286"
            y2="470"
            className="map-route map-route--drawn"
            style={{
              strokeDasharray: `${MAP_L2} ${MAP_L2}`,
              strokeDashoffset: routeOffset2,
            }}
          />
          <text x="294" y="252" className="map-label">
            {t('home.landing.map.rabat')}
          </text>
          <text x="478" y="208" className="map-label">
            {t('home.landing.map.casablanca')}
          </text>
          <text x="546" y="450" className="map-label">
            {t('home.landing.map.tantan')}
          </text>
          <text x="342" y="500" className="map-caption">
            {t('home.landing.map.goldenPath')}
          </text>
          <circle cx="330" cy="260" r="7" className="map-node map-node--main" />
          <circle cx="500" cy="220" r="5" className="map-node" />
          <circle cx="286" cy="470" r="5" className="map-node map-node--release" />
        </svg>
      </motion.div>

      <section className="obsidian-hero">
        <div className="obsidian-copy">
          <p className="obsidian-kicker">{t('home.landing.kicker')}</p>
          <h1 className="obsidian-title obsidian-title--split">
            <span className="obsidian-title__half">{t('home.landing.hero.l1')}</span>
            <span className="obsidian-title__half">{t('home.landing.hero.l2')}</span>
          </h1>
          <Reveal>
            <p className="obsidian-sub">{t('home.landing.hero.sub')}</p>
            <div className="obsidian-actions">
              <Link className="obsidian-link obsidian-link--auth" to="/login">
                {t('nav.login')}
              </Link>
              <Link className="obsidian-link obsidian-link--auth" to="/register">
                {t('nav.register')}
              </Link>
              <Link className="obsidian-link obsidian-link--live" to="/live">
                {t('home.cta.live')}
              </Link>
            </div>
          </Reveal>
        </div>
        <div className="geo-coords">
          <span>34.02° N</span>
          <span>6.83° W</span>
        </div>
      </section>

      <Reveal>
        <section className="marketing-strip" aria-label="Live federation stats">
          {t('home.landing.marketing')}
        </section>
      </Reveal>

      <section className="spatial-section" id="the-mission">
        <div className="spatial-inner spatial-inner--split">
          <div>
            <p className="obsidian-kicker">{t('home.landing.mission.kicker')}</p>
            <LineStagger className="spatial-headline spatial-headline--stagger" />
          </div>
          <Reveal>
            <div className="spatial-body copy-relaxed">
              <p>{t('home.landing.mission.body')}</p>
            </div>
          </Reveal>
        </div>
      </section>

      <section className="spatial-section" id="the-mechanics" ref={mechanicsRef}>
        <Reveal>
          <p className="obsidian-kicker">{t('home.landing.mechanics.kicker')}</p>
        </Reveal>
        <Reveal>
          <h2 className="spatial-subhead type-swiss">{t('home.landing.mechanics.subhead')}</h2>
        </Reveal>
        <div className="mechanics-grid">
          <Reveal>
            <article className="mechanics-step">
              <span className="mechanics-num">01</span>
              <h3>{t('home.landing.m1.title')}</h3>
              <p className="copy-relaxed">
                <strong className="accent-gold">{t('home.landing.m1.lead')}</strong> {t('home.landing.m1.body')}
              </p>
            </article>
          </Reveal>
          <Reveal>
            <article className="mechanics-step">
              <span className="mechanics-num">02</span>
              <h3>{t('home.landing.m2.title')}</h3>
              <p className="copy-relaxed">
                <strong className="accent-gold">{t('home.landing.m2.lead')}</strong> {t('home.landing.m2.body')}
              </p>
            </article>
          </Reveal>
          <Reveal>
            <article className="mechanics-step">
              <span className="mechanics-num">03</span>
              <h3>{t('home.landing.m3.title')}</h3>
              <p className="copy-relaxed">
                <strong className="accent-gold">{t('home.landing.m3.lead')}</strong> {t('home.landing.m3.body')}
              </p>
            </article>
          </Reveal>
        </div>
      </section>

      <section className="spatial-section" id="the-categories">
        <Reveal>
          <p className="obsidian-kicker">{t('home.landing.categories.kicker')}</p>
        </Reveal>
        <Reveal>
          <h2 className="spatial-subhead type-swiss">{t('home.landing.categories.subhead')}</h2>
        </Reveal>
        <div className="category-grid">
          <Reveal>
            <article className="category-pillar">
              <span>01</span>
              <h3 className="type-swiss">{t('home.landing.c1.title')}</h3>
              <p className="copy-relaxed">{t('home.landing.c1.body')}</p>
            </article>
          </Reveal>
          <Reveal>
            <article className="category-pillar">
              <span>02</span>
              <h3 className="type-swiss">{t('home.landing.c2.title')}</h3>
              <p className="copy-relaxed">{t('home.landing.c2.body')}</p>
            </article>
          </Reveal>
          <Reveal>
            <article className="category-pillar">
              <span>03</span>
              <h3 className="type-swiss">{t('home.landing.c3.title')}</h3>
              <p className="copy-relaxed">{t('home.landing.c3.body')}</p>
            </article>
          </Reveal>
        </div>
      </section>

      <section className="spatial-section" id="after">
        <div className="spatial-inner spatial-inner--split">
          <div>
            <p className="obsidian-kicker">{t('home.landing.after.kicker')}</p>
            <h2 className="spatial-headline type-swiss">{t('home.landing.after.title')}</h2>
          </div>
          <Reveal>
            <div className="spatial-body copy-relaxed">
              <p>{t('home.landing.after.body')}</p>
            </div>
          </Reveal>
        </div>
      </section>

      <SpatialFooter />
    </div>
  );
}
