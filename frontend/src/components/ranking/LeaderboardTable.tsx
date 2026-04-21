import { motion } from 'framer-motion';
import type { RankMovement } from '@/hooks/useRankMovement';
import { heroImageForId } from '@/components/pigeon/pigeonImagery';
import './leaderboard-table.css';

export type LeaderboardRow = {
  id: string;
  pigeonId?: string | null;
  rank: number;
  /** Primary label (ring number or fallback). */
  title: string;
  subtitle?: string;
  imageUrl?: string | null;
  points?: number | null;
  speed?: number | null;
  distance?: number | null;
  arrival?: string | null;
};

type Props = {
  rows: LeaderboardRow[];
  /** Show Δ column with arrows when movement map is provided. */
  liveMode?: boolean;
  movementByKey?: Record<string, RankMovement>;
  /** Limit visible rows (e.g. top 10 on live view). */
  maxRows?: number;
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

function rowKey(r: LeaderboardRow) {
  return r.pigeonId || r.id;
}

function movementRowClass(m: RankMovement | undefined) {
  if (m === 'up') return 'leaderboard-table__row--move-up';
  if (m === 'down') return 'leaderboard-table__row--move-down';
  return '';
}

function MovementIcon({ m }: { m: RankMovement | undefined }) {
  if (!m || m === 'same') return <span className="leaderboard-table__delta leaderboard-table__delta--same">—</span>;
  if (m === 'new') return <span className="leaderboard-table__delta leaderboard-table__delta--new">●</span>;
  if (m === 'up') return <span className="leaderboard-table__delta leaderboard-table__delta--up">↑</span>;
  return <span className="leaderboard-table__delta leaderboard-table__delta--down">↓</span>;
}

export function LeaderboardTable({ rows, liveMode = false, movementByKey, maxRows }: Props) {
  const display = typeof maxRows === 'number' ? rows.slice(0, maxRows) : rows;

  return (
    <div className="leaderboard-table-wrap">
      <table className="leaderboard-table">
        <thead>
          <tr>
            <th scope="col">Rank</th>
            <th scope="col">Athlete</th>
            {liveMode && <th scope="col">Δ</th>}
            <th scope="col">Pts</th>
            <th scope="col">Speed</th>
            <th scope="col">Dist.</th>
            <th scope="col">Arrival</th>
          </tr>
        </thead>
        <tbody>
          {display.map((r) => {
            const key = rowKey(r);
            const thumb = r.imageUrl?.trim()
              ? r.imageUrl
              : heroImageForId(r.pigeonId || r.id);
            return (
              <motion.tr
                key={r.id}
                layout="position"
                initial={false}
                animate={{ opacity: 1 }}
                transition={{ type: 'spring', stiffness: 420, damping: 32 }}
                className={`${r.rank <= 3 ? 'leaderboard-table__row--spotlight' : ''} ${movementRowClass(
                  movementByKey?.[key]
                )}`.trim()}
              >
                <td>
                  <span className={`leaderboard-table__rank ${rankClass(r.rank)}`}>#{r.rank}</span>
                </td>
                <td>
                  <div className="leaderboard-table__athlete">
                    <img src={thumb} alt="" className="leaderboard-table__thumb" loading="lazy" />
                    <div>
                      <div className="leaderboard-table__title">{r.title}</div>
                      {r.subtitle && <div className="leaderboard-table__sub">{r.subtitle}</div>}
                    </div>
                  </div>
                </td>
                {liveMode && (
                  <td className="leaderboard-table__delta-cell">
                    <MovementIcon m={movementByKey?.[key]} />
                  </td>
                )}
                <td className="leaderboard-table__num">{fmt(r.points)}</td>
                <td className="leaderboard-table__num">{fmt(r.speed)}</td>
                <td className="leaderboard-table__num">{fmt(r.distance)}</td>
                <td className="leaderboard-table__time">{fmtDate(r.arrival)}</td>
              </motion.tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
