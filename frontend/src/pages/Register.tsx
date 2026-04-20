import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';

export function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await register(name, email, password);
      navigate('/competitions', { replace: true });
    } catch {
      setError('Could not register. Email may already be in use.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="animate-in card" style={{ maxWidth: 420, margin: '2rem auto' }}>
      <h2 style={{ marginTop: 0 }}>Create account</h2>
      <p style={{ color: 'var(--muted)', marginTop: 0 }}>
        New accounts are registered as <strong>breeders</strong>. Organizers and admins are assigned by the federation.
      </p>
      <form className="stack" onSubmit={onSubmit}>
        <div>
          <label className="label" htmlFor="name">
            Name
          </label>
          <input
            id="name"
            className="input"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
            maxLength={50}
          />
        </div>
        <div>
          <label className="label" htmlFor="email">
            Email
          </label>
          <input
            id="email"
            className="input"
            type="email"
            autoComplete="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label className="label" htmlFor="password">
            Password
          </label>
          <input
            id="password"
            className="input"
            type="password"
            autoComplete="new-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={8}
          />
        </div>
        {error && <p style={{ color: '#dc2626', margin: 0 }}>{error}</p>}
        <button className="btn btn-primary" type="submit" disabled={loading}>
          {loading ? 'Creating…' : 'Register'}
        </button>
      </form>
      <p style={{ color: 'var(--muted)', marginBottom: 0 }}>
        Already have an account? <Link to="/login">Sign in</Link>
      </p>
    </div>
  );
}
