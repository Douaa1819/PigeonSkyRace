import { api } from '@/api/client';
import type { AxiosError } from 'axios';
import { useCallback, useEffect, useState } from 'react';

type State<T> = {
  data: T | null;
  loading: boolean;
  error: string | null;
  reload: () => void;
};

/**
 * @param softRefetch After the first successful load, refetches do not show the full loading state (for polling).
 */
export function useApiGet<T>(path: string, enabled = true, softRefetch = false): State<T> {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [v, setV] = useState(0);

  const reload = useCallback(() => setV((x) => x + 1), []);

  useEffect(() => {
    if (!enabled) {
      setLoading(false);
      return;
    }
    let cancelled = false;
    const showLoading = !(softRefetch && v > 0);
    if (showLoading) setLoading(true);
    setError(null);
    (async () => {
      try {
        const { data: res } = await api.get<T>(path);
        if (!cancelled) setData(res);
      } catch (e) {
        const ax = e as AxiosError<{ message?: string }>;
        const msg =
          ax.response?.data && typeof ax.response.data === 'object' && 'message' in ax.response.data
            ? String((ax.response.data as { message?: string }).message)
            : ax.message || 'Request failed';
        if (!cancelled) setError(msg);
      } finally {
        if (!cancelled) setLoading(false);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [path, enabled, v, softRefetch]);

  return { data, loading, error, reload };
}

export function usePollingApiGet<T>(path: string, intervalMs: number, enabled = true): State<T> {
  const { data, loading, error, reload } = useApiGet<T>(path, enabled, true);

  useEffect(() => {
    if (!enabled || intervalMs <= 0) return;
    const id = window.setInterval(() => reload(), intervalMs);
    return () => window.clearInterval(id);
  }, [enabled, intervalMs, reload, path]);

  return { data, loading, error, reload };
}
