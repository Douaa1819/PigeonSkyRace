import { api } from '@/api/client';
import type { AxiosError } from 'axios';
import { useCallback, useEffect, useState } from 'react';

type State<T> = {
  data: T | null;
  loading: boolean;
  error: string | null;
  reload: () => void;
};

export function useApiGet<T>(path: string, enabled = true): State<T> {
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
    setLoading(true);
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
  }, [path, enabled, v]);

  return { data, loading, error, reload };
}
