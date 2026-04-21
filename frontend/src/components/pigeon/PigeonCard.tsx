import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { GlassCard } from '@/components/ui/GlassCard';
import { heroImageForId } from '@/components/pigeon/pigeonImagery';
import './pigeon-card.css';

export type PigeonCardModel = {
  id: string;
  numeroBague: string;
  imageUrl?: string | null;
  sexe?: string | null;
  age?: number | null;
  couleur?: string | null;
  loftName?: string;
  /** Speed km/h or backend unit */
  speed?: number | null;
  points?: number | null;
  rank?: number;
};

type Props = {
  pigeon: PigeonCardModel;
  index?: number;
  /** Shown above the ring value (default: Ring). */
  idLabel?: string;
};

export function PigeonCard({ pigeon, index = 0, idLabel = 'Ring' }: Props) {
  const img = pigeon.imageUrl?.trim() ? pigeon.imageUrl : heroImageForId(pigeon.id);
  const rank = pigeon.rank;
  const top = rank === 1 || rank === 2 || rank === 3;

  return (
    <motion.div
      initial={{ opacity: 0, y: 24 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: index * 0.06, duration: 0.35, ease: [0.22, 1, 0.36, 1] }}
    >
      <GlassCard className="pigeon-card">
        <div className="pigeon-card__media">
          <img src={img} alt="" className="pigeon-card__img" loading="lazy" />
          <div className="pigeon-card__media-gradient" />
          {rank != null && (
            <span className={`pigeon-card__badge pigeon-card__badge--${top ? `top-${rank}` : 'rest'}`}>
              #{rank}
            </span>
          )}
        </div>
        <div className="pigeon-card__body">
          <div className="pigeon-card__ring">
            <span className="pigeon-card__ring-label">{idLabel}</span>
            <span className="pigeon-card__ring-value">{pigeon.numeroBague}</span>
          </div>
          {pigeon.loftName && <p className="pigeon-card__loft">{pigeon.loftName}</p>}
          <div className="pigeon-card__stats">
            {pigeon.speed != null && (
              <div>
                <span className="pigeon-card__stat-label">Speed</span>
                <span className="pigeon-card__stat-val">{pigeon.speed.toFixed(2)}</span>
              </div>
            )}
            {pigeon.points != null && (
              <div>
                <span className="pigeon-card__stat-label">Pts</span>
                <span className="pigeon-card__stat-val">{pigeon.points.toFixed(2)}</span>
              </div>
            )}
            {pigeon.couleur && (
              <div>
                <span className="pigeon-card__stat-label">Color</span>
                <span className="pigeon-card__stat-val pigeon-card__stat-val--sm">{pigeon.couleur}</span>
              </div>
            )}
          </div>
          <Link className="pigeon-card__analytics" to={`/pigeons/${pigeon.id}/analytics`}>
            Performance charts
          </Link>
        </div>
      </GlassCard>
    </motion.div>
  );
}
