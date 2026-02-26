import { Navigate, Route, Routes } from 'react-router-dom';
import Dashboard from '../pages/Dashboard';
import Graph from '../pages/Graph';
import Metrics from '../pages/Metrics';
import Violations from '../pages/Violations';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/metrics" element={<Metrics />} />
      <Route path="/violations" element={<Violations />} />
      <Route path="/graph" element={<Graph />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
