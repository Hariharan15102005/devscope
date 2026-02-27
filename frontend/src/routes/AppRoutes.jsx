import { Navigate, Route, Routes } from 'react-router-dom';
import ComparePage from '../pages/ComparePage';
import Dashboard from '../pages/Dashboard';
import DependencyGraphPage from '../pages/DependencyGraphPage';
import InsightsPage from '../pages/InsightsPage';
import Metrics from '../pages/Metrics';
import StructureExplorer from '../pages/StructureExplorer';
import ViolationsPage from '../pages/ViolationsPage';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/metrics" element={<Metrics />} />
      <Route path="/violations" element={<ViolationsPage />} />
      <Route path="/graph" element={<DependencyGraphPage />} />
      <Route path="/insights" element={<InsightsPage />} />
      <Route path="/compare" element={<ComparePage />} />
      <Route path="/structure" element={<StructureExplorer />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
