import { Navigate, Route, Routes } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { useAuth } from '@/context/AuthContext';
import { BreederDashboard } from '@/pages/BreederDashboard';
import { Competitions } from '@/pages/Competitions';
import { Home } from '@/pages/Home';
import { Login } from '@/pages/Login';
import { OrganizerDashboard } from '@/pages/OrganizerDashboard';
import { Pigeons } from '@/pages/Pigeons';
import { Register } from '@/pages/Register';
import { Results } from '@/pages/Results';
import type { ReactNode } from 'react';

function RequireAuth({ children }: { children: ReactNode }) {
  const { user, token } = useAuth();
  if (!token || !user) {
    return <Navigate to="/login" replace />;
  }
  return children;
}

function RequireOrganizer({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.role !== 'ADMIN' && user.role !== 'ORGANIZER') {
    return <Navigate to="/" replace />;
  }
  return children;
}

function RequireBreeder({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.role !== 'BREEDER') {
    return <Navigate to="/" replace />;
  }
  return children;
}

export function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/organizer"
          element={
            <RequireAuth>
              <RequireOrganizer>
                <OrganizerDashboard />
              </RequireOrganizer>
            </RequireAuth>
          }
        />
        <Route
          path="/breeder"
          element={
            <RequireAuth>
              <RequireBreeder>
                <BreederDashboard />
              </RequireBreeder>
            </RequireAuth>
          }
        />
        <Route
          path="/competitions"
          element={
            <RequireAuth>
              <Competitions />
            </RequireAuth>
          }
        />
        <Route
          path="/pigeons"
          element={
            <RequireAuth>
              <Pigeons />
            </RequireAuth>
          }
        />
        <Route
          path="/results"
          element={
            <RequireAuth>
              <Results />
            </RequireAuth>
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}
