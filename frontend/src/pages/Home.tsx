import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { GlassCard } from '@/components/ui/GlassCard';

const fade = {
  initial: { opacity: 0, y: 12 },
  animate: { opacity: 1, y: 0 },
};

export function Home() {
  return (
    <div className="home-hero stack" style={{ gap: '1.5rem' }}>
      <motion.div {...fade} transition={{ duration: 0.4, ease: [0.22, 1, 0.36, 1] }}>
        <p
          style={{
            margin: 0,
            fontSize: '0.8rem',
            fontWeight: 700,
            letterSpacing: '0.14em',
            textTransform: 'uppercase',
            color: 'var(--gold)',
            textShadow: '0 0 20px var(--gold-soft)',
          }}
        >
          Elite pigeon athletics
        </p>
        <h1 style={{ fontSize: 'clamp(2rem, 5vw, 2.75rem)', margin: '0.35rem 0', letterSpacing: '-0.03em' }}>
          Race the sky. Lead the leaderboard.
        </h1>
        <p style={{ color: 'var(--muted)', margin: 0, maxWidth: '52ch', fontSize: '1.05rem' }}>
          A premium sports platform for Moroccan pigeon racing — seasons, lofts, ringed athletes, and live
          standings with motion, depth, and glass UI.
        </p>
        <div className="home-hero__actions">
          <Link className="btn btn-primary" to="/login">
            Sign in
          </Link>
          <Link className="btn btn-ghost" to="/register">
            Create breeder account
          </Link>
        </div>
      </motion.div>

      <motion.div
        className="home-features"
        initial="hidden"
        animate="show"
        variants={{
          hidden: {},
          show: {
            transition: { staggerChildren: 0.08 },
          },
        }}
      >
        {[
          {
            t: 'Competition engine',
            d: 'Organize seasons and races with federation-grade structure.',
          },
          {
            t: 'Athlete profiles',
            d: 'Every pigeon is a competitor — ring ID, loft, speed, and points.',
          },
          {
            t: 'Live rankings',
            d: 'Animated leaderboards with top-three spotlight and glass panels.',
          },
        ].map((x) => (
          <motion.div key={x.t} variants={{ hidden: { opacity: 0, y: 10 }, show: { opacity: 1, y: 0 } }}>
            <GlassCard className="home-feature" hoverLift>
              <h3>{x.t}</h3>
              <p>{x.d}</p>
            </GlassCard>
          </motion.div>
        ))}
      </motion.div>
    </div>
  );
}
