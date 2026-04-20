export function Results() {
  return (
    <div className="animate-in stack">
      <h1 style={{ margin: 0 }}>Results & rankings</h1>
      <p style={{ color: 'var(--muted)', margin: 0 }}>
        Fetch results per competition (<code>GET /api/v1/resultats/&#123;competitionId&#125;</code>) and export PDF when needed.
      </p>
      <div className="card">
        <p style={{ margin: 0, color: 'var(--muted)' }}>Placeholder — add competition selector and results table next.</p>
      </div>
    </div>
  );
}
