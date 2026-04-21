import { useEffect, useRef, useState } from 'react';

export type RankMovement = 'up' | 'down' | 'same' | 'new';

type RowKey = { pigeonId?: string | null; id: string; rank: number };

/**
 * Compares successive poll snapshots and returns per-row rank movement (lower rank number = better).
 * @param resetKey When this changes (e.g. competition id), baseline is reset.
 */
export function useRankMovement(rows: RowKey[], resetKey: string) {
  const prev = useRef<Map<string, number>>(new Map());
  const first = useRef(true);
  const [movementByKey, setMovementByKey] = useState<Record<string, RankMovement>>({});
  const lastReset = useRef(resetKey);

  useEffect(() => {
    if (lastReset.current !== resetKey) {
      lastReset.current = resetKey;
      first.current = true;
      prev.current = new Map();
    }
  }, [resetKey]);

  useEffect(() => {
    if (first.current) {
      first.current = false;
      prev.current = new Map(rows.map((r) => [r.pigeonId || r.id, r.rank]));
      setMovementByKey({});
      return;
    }

    const nextMap: Record<string, RankMovement> = {};
    const nextPrev = new Map<string, number>();

    for (const r of rows) {
      const key = r.pigeonId || r.id;
      const oldRank = prev.current.get(key);
      const rank = r.rank;
      let m: RankMovement = 'same';
      if (oldRank === undefined) {
        m = 'new';
      } else if (rank < oldRank) {
        m = 'up';
      } else if (rank > oldRank) {
        m = 'down';
      }
      nextMap[key] = m;
      nextPrev.set(key, rank);
    }

    prev.current = nextPrev;
    setMovementByKey(nextMap);
  }, [rows]);

  return movementByKey;
}
