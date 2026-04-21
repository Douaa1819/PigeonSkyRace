import type { ResultatDto } from '@/api/types';
import type { LeaderboardRow } from '@/components/ranking/LeaderboardTable';

export function resultatsToLeaderboardRows(results: ResultatDto[]): LeaderboardRow[] {
  const sorted = [...results].sort((a, b) => {
    const ra = a.rank ?? 99999;
    const rb = b.rank ?? 99999;
    if (ra !== rb) return ra - rb;
    const pa = a.points ?? Number.NEGATIVE_INFINITY;
    const pb = b.points ?? Number.NEGATIVE_INFINITY;
    return pb - pa;
  });

  return sorted.map((r, i) => ({
    id: r.id,
    pigeonId: r.pigeonId,
    rank: r.rank ?? i + 1,
    title: r.ringNumber?.trim() ? r.ringNumber : `Result ${r.id.slice(-6)}`,
    subtitle: r.loftName ?? undefined,
    imageUrl: r.imageUrl,
    points: r.points,
    speed: r.vitesse,
    distance: r.distance,
    arrival: r.dateArrivee,
  }));
}
