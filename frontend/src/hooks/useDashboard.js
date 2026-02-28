import { useCallback, useEffect, useState } from 'react';
import dashboardApi from '../api/dashboardApi';

export default function useDashboard(analysisId) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const fetchDashboard = useCallback(async () => {
    if (!analysisId) {
      setData(null);
      return;
    }

    try {
      setLoading(true);
      setError('');
      const summary = await dashboardApi.getSummary(analysisId);
      setData(summary);
    } catch (err) {
      setError(err.message || 'Unable to load dashboard data.');
    } finally {
      setLoading(false);
    }
  }, [analysisId]);

  useEffect(() => {
    fetchDashboard();
  }, [fetchDashboard]);

  return {
    data,
    loading,
    error,
    refetch: fetchDashboard,
  };
}
