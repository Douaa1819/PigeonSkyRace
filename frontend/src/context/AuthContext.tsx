import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import { api, getStoredToken, setStoredToken, type UserDto } from '@/api/client';

type AuthContextValue = {
  user: UserDto | null;
  token: string | null;
  login: (email: string, password: string) => Promise<UserDto>;
  register: (name: string, email: string, password: string) => Promise<UserDto>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextValue | null>(null);
const USER_KEY = 'psr_user';

function readUser(): UserDto | null {
  try {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    return JSON.parse(raw) as UserDto;
  } catch {
    return null;
  }
}

function writeUser(u: UserDto | null) {
  if (u) localStorage.setItem(USER_KEY, JSON.stringify(u));
  else localStorage.removeItem(USER_KEY);
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<UserDto | null>(() => readUser());
  const [token, setToken] = useState<string | null>(() => getStoredToken());

  const login = useCallback(async (email: string, password: string) => {
    const { data } = await api.post<{ accessToken: string; user: UserDto }>('/v1/auth/login', {
      email,
      password,
    });
    setStoredToken(data.accessToken);
    setToken(data.accessToken);
    setUser(data.user);
    writeUser(data.user);
    return data.user;
  }, []);

  const register = useCallback(async (name: string, email: string, password: string) => {
    const { data } = await api.post<{ accessToken: string; user: UserDto }>('/v1/auth/register', {
      name,
      email,
      password,
    });
    setStoredToken(data.accessToken);
    setToken(data.accessToken);
    setUser(data.user);
    writeUser(data.user);
    return data.user;
  }, []);

  const logout = useCallback(() => {
    setStoredToken(null);
    setToken(null);
    setUser(null);
    writeUser(null);
    void api.post('/v1/auth/logout').catch(() => {});
  }, []);

  const value = useMemo(
    () => ({ user, token, login, register, logout }),
    [user, token, login, register, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
