import { Navigate, Route, Routes } from 'react-router-dom';
import Dashboard from '../pages/Dashboard';
import DependencyGraphPage from '../pages/DependencyGraphPage';
import Insights from '../pages/Insights';
import Metrics from '../pages/Metrics';
import StructureExplorer from '../pages/StructureExplorer';
import Violations from '../pages/Violations';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/metrics" element={<Metrics />} />
      <Route path="/violations" element={<Violations />} />
      <Route path="/graph" element={<DependencyGraphPage />} />
      <Route path="/insights" element={<Insights />} />
      <Route path="/structure" element={<StructureExplorer />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
