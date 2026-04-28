import axios from 'axios';

const TOKEN_KEY = 'psr_access_token';

export const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

export function getStoredToken(): string | null {
  if (typeof window === 'undefined') return null;
  return localStorage.getItem(TOKEN_KEY);
}

export function setStoredToken(token: string | null) {
  if (typeof window === 'undefined') return;
  if (token) {
    localStorage.setItem(TOKEN_KEY, token);
  } else {
    localStorage.removeItem(TOKEN_KEY);
  }
}

api.interceptors.request.use((config) => {
  const t = getStoredToken();
  if (t) {
    config.headers.Authorization = `Bearer ${t}`;
  }
  return config;
});

function clearSessionAndRedirectLogin() {
  if (typeof window === 'undefined') return;
  setStoredToken(null);
  localStorage.removeItem('psr_user');
  const path = window.location.pathname;
  if (path !== '/login' && path !== '/register') {
    window.location.assign('/login');
  }
}

api.interceptors.response.use(
  (r) => r,
  (err) => {
    const url = err.config?.url ?? '';
    const isLoginOrRegister =
      url.includes('/v1/auth/login') || url.includes('/v1/auth/register');
    if (err.response?.status === 401 && !isLoginOrRegister) {
      clearSessionAndRedirectLogin();
    }
    return Promise.reject(err);
  }
);

export type Role = 'ADMIN' | 'ORGANIZER' | 'BREEDER';

export type UserDto = {
  id: string;
  name: string;
  email: string;
  role: Role;
};

export type AuthResponse = {
  accessToken: string;
  user: UserDto;
};
