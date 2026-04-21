import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { GlassCard } from '@/components/ui/GlassCard';

const fade = {
  initial: { opacity: 0, y: 12 },
  animate: { opacity: 1, y: 0 },
};

export function Home() {
  return (
    <div className="home stack" style={{ gap: '1.85rem' }}>
      <motion.section className="home-hero" {...fade} transition={{ duration: 0.42, ease: [0.22, 1, 0.36, 1] }}>
        <span className="home-kicker">Professional Moroccan Federation Platform</span>
        <h1 className="home-hero-title">Race the Sky. Broadcast Every Ranking Live.</h1>
        <p className="home-hero-sub">
          PigeonSkyRace is a production-ready SaaS platform for organizing Moroccan racing pigeon competitions:
          breeders, organizers, race operations, live leaderboards, analytics, and official results in one secure
          workspace.
        </p>
        <div className="home-hero__actions">
          <Link className="btn btn-primary" to="/login">
            Login
          </Link>
          <Link className="btn btn-ghost" to="/register">
            Register
          </Link>
          <Link className="btn btn-ghost" to="/live">
            Live Rankings
          </Link>
        </div>
      </motion.section>

      <motion.section className="home-features" initial="hidden" animate="show" variants={{ hidden: {}, show: { transition: { staggerChildren: 0.06 } } }}>
        {[
          {
            t: 'Live race telemetry',
            d: 'Stream ranking updates in realtime using resilient WebSocket and fallback polling transport.',
          },
          {
            t: 'Competition operations',
            d: 'Manage seasons, races, entries, and race status with organizer-grade dashboards.',
          },
          {
            t: 'Athlete intelligence',
            d: 'Track each pigeon with profile cards, performance history, and analytics views.',
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
          <h3>How it works</h3>
          <ol className="home-list">
            <li>Organizers create competitions and race windows.</li>
            <li>Breeders register pigeons and track participation.</li>
            <li>Results are computed, ranked, and broadcast live.</li>
          </ol>
        </GlassCard>
        <GlassCard className="home-section-card" hoverLift={false}>
          <h3>Why teams choose it</h3>
          <ul className="home-list">
            <li>Live leaderboard visibility with premium UX.</li>
            <li>Role-based routing for admins, organizers, and breeders.</li>
            <li>Data-ready architecture for future AI prediction modules.</li>
          </ul>
        </GlassCard>
      </section>

      <section className="home-grid home-grid--2">
        <GlassCard className="home-preview" hoverLift={false}>
          <h3>Dashboard preview</h3>
          <p className="muted">Organizer control, breeder athlete cards, and race broadcast view.</p>
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
          <h3>Trusted competition flow</h3>
          <p className="muted">Designed for consistency, clarity, and fair ranking transparency.</p>
          <div className="home-preview__kpis">
            <div>
              <strong>Realtime</strong>
              <span>WS + Fallback</span>
            </div>
            <div>
              <strong>Role-safe</strong>
              <span>JWT secured</span>
            </div>
            <div>
              <strong>Analytics</strong>
              <span>Performance history</span>
            </div>
          </div>
        </GlassCard>
      </section>

      <footer className="home-footer muted">
        PigeonSkyRace — professional SaaS for Moroccan racing pigeon competitions.
      </footer>
    </div>
  );
}
