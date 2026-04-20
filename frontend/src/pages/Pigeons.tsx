export function Pigeons() {
  return (
    <div className="animate-in stack">
      <h1 style={{ margin: 0 }}>Pigeons</h1>
      <p style={{ color: 'var(--muted)', margin: 0 }}>
        List and register pigeons (GET/POST <code>/api/v1/pigeons</code>). Wire forms here in the next iteration.
      </p>
      <div className="card">
        <p style={{ margin: 0, color: 'var(--muted)' }}>Placeholder — connect to your API with the shared Axios client.</p>
      </div>
    </div>
  );
}
