import type { CompetitionDto } from '@/api/types';

/** True when current time is within [startTime, endTime] (inclusive). */
export function competitionIsLive(c: CompetitionDto | undefined): boolean {
  if (!c?.startTime || !c?.endTime) return false;
  const now = Date.now();
  const start = new Date(c.startTime).getTime();
  const end = new Date(c.endTime).getTime();
  if (Number.isNaN(start) || Number.isNaN(end)) return false;
  return now >= start && now <= end;
}
