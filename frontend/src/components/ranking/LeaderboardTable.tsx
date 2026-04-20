import { motion } from 'framer-motion';
import { heroImageForId } from '@/components/pigeon/pigeonImagery';
import './leaderboard-table.css';

export type LeaderboardRow = {
  id: string;
  rank: number;
  /** Primary label (e.g. ring number or finisher label). */
  title: string;
  subtitle?: string;
  points?: number | null;
  speed?: number | null;
  distance?: number | null;
  arrival?: string | null;
};

type Props = {
  rows: LeaderboardRow[];
};

function rankClass(rank: number) {
  if (rank === 1) return 'leaderboard-table__rank--gold';
  if (rank === 2) return 'leaderboard-table__rank--silver';
  if (rank === 3) return 'leaderboard-table__rank--bronze';
  return '';
}

function fmt(n: number | null | undefined, digits = 2) {
  if (n == null || Number.isNaN(n)) return '—';
  return n.toFixed(digits);
}

function fmtDate(s: string | null | undefined) {
  if (!s) return '—';
  const d = new Date(s);
  if (Number.isNaN(d.getTime())) return s;
  return d.toLocaleString(undefined, { dateStyle: 'medium', timeStyle: 'short' });
}

export function LeaderboardTable({ rows }: Props) {
  return (
    <div className="leaderboard-table-wrap">
      <table className="leaderboard-table">
        <thead>
          <tr>
            <th scope="col">Rank</th>
            <th scope="col">Athlete</th>
            <th scope="col">Pts</th>
            <th scope="col">Speed</th>
            <th scope="col">Dist.</th>
            <th scope="col">Arrival</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((r, i) => (
            <motion.tr
              key={r.id}
              layout
              initial={{ opacity: 0, x: -8 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: i * 0.04, duration: 0.28, ease: [0.22, 1, 0.36, 1] }}
              className={r.rank <= 3 ? 'leaderboard-table__row--spotlight' : undefined}
            >
              <td>
                <span className={`leaderboard-table__rank ${rankClass(r.rank)}`}>#{r.rank}</span>
              </td>
              <td>
                <div className="leaderboard-table__athlete">
                  <img
                    src={heroImageForId(r.id)}
                    alt=""
                    className="leaderboard-table__thumb"
                    loading="lazy"
                  />
                  <div>
                    <div className="leaderboard-table__title">{r.title}</div>
                    {r.subtitle && <div className="leaderboard-table__sub">{r.subtitle}</div>}
                  </div>
                </div>
              </td>
              <td className="leaderboard-table__num">{fmt(r.points)}</td>
              <td className="leaderboard-table__num">{fmt(r.speed)}</td>
              <td className="leaderboard-table__num">{fmt(r.distance)}</td>
              <td className="leaderboard-table__time">{fmtDate(r.arrival)}</td>
            </motion.tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
