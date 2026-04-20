import axios from 'axios';

const TOKEN_KEY = 'psr_access_token';

export const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

export function getStoredToken(): string | null {
  return localStorage.getItem(TOKEN_KEY);
}

export function setStoredToken(token: string | null) {
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

api.interceptors.response.use(
  (r) => r,
  (err) => {
    if (err.response?.status === 401) {
      setStoredToken(null);
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
  tokenType: string;
  expiresInMs: number;
  user: UserDto;
};
