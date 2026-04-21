import { useMemo } from 'react';
import { Link, useParams } from 'react-router-dom';
import type { PigeonResultHistoryItemDto } from '@/api/types';
import { GlassCard } from '@/components/ui/GlassCard';
import { SectionTitle } from '@/components/ui/SectionTitle';
import { Skeleton } from '@/components/ui/Skeleton';
import { useApiGet } from '@/hooks/useApiGet';
import {
  CartesianGrid,
  Legend,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts';

export function PigeonAnalytics() {
  const { id } = useParams();
  const pigeonId = id ?? '';

  const { data, loading, error } = useApiGet<PigeonResultHistoryItemDto[]>(
    `/v1/pigeons/${encodeURIComponent(pigeonId)}/result-history`,
    !!pigeonId
  );

  const chartData = useMemo(() => {
    if (!data?.length) return [];
    return data.map((d, i) => ({
      label: d.competitionName?.length ? d.competitionName.slice(0, 14) : `Race ${i + 1}`,
      points: d.points ?? 0,
      vitesse: d.vitesse ?? 0,
      rank: d.rank ?? null,
    }));
  }, [data]);

  return (
    <div className="stack" style={{ gap: '1.5rem' }}>
      <SectionTitle
        eyebrow="Analytics"
        title="Performance history"
        subtitle="Speed and points by competition — powered by your API history endpoint."
      />

      {error && <div className="error-banner">{error}</div>}

      {loading && (
        <div className="stack" style={{ gap: '0.65rem' }}>
          <Skeleton height={280} />
        </div>
      )}

      {!loading && !error && (
        <>
          {chartData.length === 0 ? (
            <GlassCard style={{ padding: '1.5rem' }}>
              <p className="muted" style={{ margin: 0 }}>
                No finished races linked to this pigeon yet.
              </p>
            </GlassCard>
          ) : (
            <GlassCard style={{ padding: '1rem 1rem 0.5rem' }} hoverLift={false}>
              <h3 style={{ margin: '0 0 0.75rem', fontSize: '1rem' }}>Speed & points</h3>
              <div style={{ width: '100%', height: 300 }}>
                <ResponsiveContainer>
                  <LineChart data={chartData} margin={{ top: 8, right: 12, left: 0, bottom: 8 }}>
                    <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.08)" />
                    <XAxis dataKey="label" tick={{ fill: 'var(--muted)', fontSize: 11 }} />
                    <YAxis yAxisId="left" tick={{ fill: 'var(--muted)', fontSize: 11 }} />
                    <YAxis yAxisId="right" orientation="right" tick={{ fill: 'var(--muted)', fontSize: 11 }} />
                    <Tooltip
                      contentStyle={{
                        background: 'rgba(15,23,42,0.92)',
                        border: '1px solid var(--glass-border)',
                        borderRadius: 12,
                      }}
                    />
                    <Legend />
                    <Line
                      yAxisId="left"
                      type="monotone"
                      dataKey="vitesse"
                      name="Speed"
                      stroke="#38bdf8"
                      strokeWidth={2}
                      dot={false}
                    />
                    <Line
                      yAxisId="right"
                      type="monotone"
                      dataKey="points"
                      name="Points"
                      stroke="#d4af37"
                      strokeWidth={2}
                      dot={false}
                    />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </GlassCard>
          )}

          <p className="muted" style={{ margin: 0 }}>
            <Link to="/pigeons">Back to pigeons</Link>
          </p>
        </>
      )}
    </div>
  );
}
