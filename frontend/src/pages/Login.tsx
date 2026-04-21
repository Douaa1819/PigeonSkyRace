import { motion } from 'framer-motion';
import { isAxiosError } from 'axios';
import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { GlassCard } from '@/components/ui/GlassCard';
import { useAuth } from '@/context/AuthContext';
import { useLocale } from '@/context/LocaleContext';

function routeForRole(role: string) {
  if (role === 'ADMIN') return '/admin';
  if (role === 'ORGANIZER') return '/organizer';
  if (role === 'BREEDER') return '/breeder';
  return '/competitions';
}

export function Login() {
  const { login } = useAuth();
  const { t } = useLocale();
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const currentUser = await login(email, password);
      navigate(routeForRole(currentUser.role), { replace: true });
    } catch (err) {
      if (isAxiosError<{ message?: string }>(err)) {
        setError(err.response?.data?.message ?? 'Invalid email or password.');
      } else {
        setError('Invalid email or password.');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <motion.div initial={{ opacity: 0, y: 18 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.35 }}>
      <GlassCard className="auth-shell" hoverLift={false}>
        <h2>{t('auth.login.title')}</h2>
        <p style={{ color: 'var(--muted)', marginTop: 0 }}>{t('auth.login.subtitle')}</p>
        <form className="stack" onSubmit={onSubmit}>
          <div>
            <label className="label" htmlFor="email">
              {t('auth.email')}
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
              {t('auth.password')}
            </label>
            <input
              id="password"
              className="input"
              type="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={8}
            />
          </div>
          {error && <p style={{ color: '#fecaca', margin: 0 }}>{error}</p>}
          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? t('auth.login.loading') : t('auth.login.submit')}
          </button>
        </form>
        <p style={{ color: 'var(--muted)', marginBottom: 0 }}>
          {t('auth.login.noAccount')} <Link to="/register">{t('nav.register')}</Link>
        </p>
      </GlassCard>
    </motion.div>
  );
}
