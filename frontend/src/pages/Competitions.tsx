import { useEffect, useState } from 'react';
import { api } from '@/api/client';

type Competition = {
  id: string;
  nom: string;
  saisonId?: string;
};

export function Competitions() {
  const [items, setItems] = useState<Competition[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const { data } = await api.get<Competition[]>('/v1/competions');
        if (!cancelled) setItems(data);
      } catch {
        if (!cancelled) setError('Sign in to load competitions.');
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  if (loading) return <p style={{ color: 'var(--muted)' }}>Loading…</p>;
  if (error) return <p style={{ color: 'var(--muted)' }}>{error}</p>;

  return (
    <div className="animate-in stack">
      <h1 style={{ margin: 0 }}>Competitions</h1>
      <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ background: 'var(--bg)', textAlign: 'left' }}>
              <th style={{ padding: '0.75rem 1rem' }}>Name</th>
              <th style={{ padding: '0.75rem 1rem' }}>Id</th>
            </tr>
          </thead>
          <tbody>
            {items.map((c) => (
              <tr key={c.id} style={{ borderTop: '1px solid var(--border)' }}>
                <td style={{ padding: '0.75rem 1rem' }}>{c.nom}</td>
                <td style={{ padding: '0.75rem 1rem', color: 'var(--muted)', fontSize: '0.85rem' }}>{c.id}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {items.length === 0 && (
          <p style={{ padding: '1rem', margin: 0, color: 'var(--muted)' }}>No competitions yet.</p>
        )}
      </div>
    </div>
  );
}
