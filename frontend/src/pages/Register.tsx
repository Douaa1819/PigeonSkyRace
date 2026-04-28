import { motion } from 'framer-motion';
import { isAxiosError } from 'axios';
import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import { useLocale } from '@/context/LocaleContext';

function routeForRole(role: string) {
  if (role === 'ADMIN') return '/admin';
  if (role === 'ORGANIZER') return '/organizer';
  if (role === 'BREEDER') return '/breeder';
  return '/competitions';
}

export function Register() {
  const { register } = useAuth();
  const { t } = useLocale();
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
      const currentUser = await register(name.trim(), email.trim(), password);
      navigate(routeForRole(currentUser.role), { replace: true });
    } catch (err) {
      if (isAxiosError<{ message?: string }>(err)) {
        setError(err.response?.data?.message ?? 'Could not register. Email may already be in use.');
      } else {
        setError('Could not register. Email may already be in use.');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <motion.div className="auth-page" initial={{ opacity: 0, y: 18 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.35 }}>
      <div className="auth-shell auth-shell--lux bg-white text-black dark:bg-black dark:text-white">
        <h2 className="dark:text-white">{t('auth.register.title')}</h2>
        <p className="text-zinc-600 dark:text-zinc-300" style={{ marginTop: 0 }}>
          {t('auth.register.subtitle')}
        </p>
        <form className="stack" onSubmit={onSubmit}>
          <div className="field-underline">
            <label className="label" htmlFor="name">
              {t('auth.name')}
            </label>
            <input
              id="name"
              className="input input--lux bg-transparent text-black dark:text-white"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              maxLength={50}
            />
            <span className="input-focus-line" />
          </div>
          <div className="field-underline">
            <label className="label" htmlFor="email">
              {t('auth.email')}
            </label>
            <input
              id="email"
              className="input input--lux bg-transparent text-black dark:text-white"
              type="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <span className="input-focus-line" />
          </div>
          <div className="field-underline">
            <label className="label" htmlFor="password">
              {t('auth.password')}
            </label>
            <input
              id="password"
              className="input input--lux bg-transparent text-black dark:text-white"
              type="password"
              autoComplete="new-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={8}
            />
            <span className="input-focus-line" />
          </div>
          {error && <p style={{ color: '#fecaca', margin: 0 }}>{error}</p>}
          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? t('auth.register.loading') : 'REJOINDRE LA FÉDÉRATION'}
          </button>
        </form>
        <p style={{ color: 'var(--muted)', marginBottom: 0 }}>
          {t('auth.register.hasAccount')} <Link to="/login">{t('nav.login')}</Link>
        </p>
      </div>
    </motion.div>
  );
}
