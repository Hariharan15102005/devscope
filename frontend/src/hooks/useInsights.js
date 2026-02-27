import { useCallback, useEffect, useMemo, useState } from 'react';
import insightApi from '../api/insightApi';

const defaultFilters = {
  impact: 'All',
  category: 'All',
  search: '',
  className: '',
};

const allowedImpact = ['HIGH', 'MEDIUM', 'LOW'];
const allowedCategory = ['COMPLEXITY', 'COUPLING', 'LAYERING', 'CYCLE'];

export default function useInsights(projectId) {
  const [insights, setInsights] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [filters, setFilters] = useState(defaultFilters);
  const [selectedInsightId, setSelectedInsightId] = useState(null);

  const refetch = useCallback(async () => {
    if (!projectId) {
      setInsights([]);
      return;
    }

    try {
      setLoading(true);
      setError('');
      const response = await insightApi.getInsights(projectId);
      setInsights(Array.isArray(response) ? response : []);
    } catch (err) {
      setError(err.message || 'Unable to load insights.');
    } finally {
      setLoading(false);
    }
  }, [projectId]);

  useEffect(() => {
    refetch();
  }, [refetch]);

  const filteredInsights = useMemo(() => {
    const searchTerm = filters.search.trim().toLowerCase();
    const classTerm = filters.className.trim().toLowerCase();

    return insights.filter((item) => {
      if (filters.impact !== 'All' && item.impactLevel !== filters.impact) {
        return false;
      }

      if (filters.category !== 'All' && item.category !== filters.category) {
        return false;
      }

      if (searchTerm) {
        const titleMatch = item.title?.toLowerCase().includes(searchTerm);
        const classMatch = (item.affectedClasses || []).some((className) => className.toLowerCase().includes(searchTerm));
        if (!titleMatch && !classMatch) {
          return false;
        }
      }

      if (classTerm) {
        const classMatch = (item.affectedClasses || []).some((className) => className.toLowerCase().includes(classTerm));
        if (!classMatch) {
          return false;
        }
      }

      return true;
    });
  }, [insights, filters]);

  const selectedInsight = useMemo(() => {
    return filteredInsights.find((item) => item.id === selectedInsightId) || null;
  }, [filteredInsights, selectedInsightId]);

  const summary = useMemo(() => {
    const high = filteredInsights.filter((item) => item.impactLevel === 'HIGH').length;
    const medium = filteredInsights.filter((item) => item.impactLevel === 'MEDIUM').length;
    const low = filteredInsights.filter((item) => item.impactLevel === 'LOW').length;

    return {
      total: filteredInsights.length,
      high,
      medium,
      low,
    };
  }, [filteredInsights]);

  function clearFilters() {
    setFilters(defaultFilters);
    setSelectedInsightId(null);
  }

  function applyQueryParams(search) {
    const params = new URLSearchParams(search);
    const className = params.get('class') || '';
    const impact = (params.get('impact') || '').toUpperCase();
    const category = (params.get('category') || '').toUpperCase();

    setFilters((prev) => ({
      ...prev,
      className,
      impact: allowedImpact.includes(impact) ? impact : 'All',
      category: allowedCategory.includes(category) ? category : 'All',
    }));

    setSelectedInsightId(null);
  }

  return {
    loading,
    error,
    refetch,
    filters,
    setFilters,
    clearFilters,
    filteredInsights,
    selectedInsight,
    selectedInsightId,
    setSelectedInsightId,
    summary,
    applyQueryParams,
  };
}
