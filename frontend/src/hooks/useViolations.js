import { useCallback, useEffect, useMemo, useState } from 'react';
import violationApi from '../api/violationApi';

const defaultFilters = {
  severity: 'All',
  type: 'All',
  search: '',
  className: '',
};

const severityWeight = {
  HIGH: 3,
  MEDIUM: 2,
  LOW: 1,
};

export default function useViolations(analysisId) {
  const [violations, setViolations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [filters, setFilters] = useState(defaultFilters);
  const [selectedViolationId, setSelectedViolationId] = useState(null);
  const [severitySortDirection, setSeveritySortDirection] = useState('desc');

  const refetch = useCallback(async () => {
    if (!analysisId) {
      setViolations([]);
      return;
    }

    try {
      setLoading(true);
      setError('');
      const data = await violationApi.getViolations(analysisId);
      setViolations(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || 'Unable to load violations.');
    } finally {
      setLoading(false);
    }
  }, [analysisId]);

  useEffect(() => {
    refetch();
  }, [refetch]);

  const filteredViolations = useMemo(() => {
    const searchTerm = filters.search.trim().toLowerCase();
    const classTerm = filters.className.trim().toLowerCase();

    const filtered = violations.filter((item) => {
      if (filters.severity !== 'All' && item.severity !== filters.severity) {
        return false;
      }

      if (filters.type !== 'All' && item.type !== filters.type) {
        return false;
      }

      if (searchTerm) {
        const inTitle = item.title?.toLowerCase().includes(searchTerm);
        const inClasses = (item.affectedClasses || []).some((className) => className.toLowerCase().includes(searchTerm));
        if (!inTitle && !inClasses) {
          return false;
        }
      }

      if (classTerm) {
        const hasClass = (item.affectedClasses || []).some((className) => className.toLowerCase().includes(classTerm));
        if (!hasClass) {
          return false;
        }
      }

      return true;
    });

    return filtered.sort((first, second) => {
      const firstWeight = severityWeight[first.severity] || 0;
      const secondWeight = severityWeight[second.severity] || 0;
      return severitySortDirection === 'desc' ? secondWeight - firstWeight : firstWeight - secondWeight;
    });
  }, [violations, filters, severitySortDirection]);

  const selectedViolation = useMemo(() => {
    return filteredViolations.find((item) => item.id === selectedViolationId) || null;
  }, [filteredViolations, selectedViolationId]);

  const summary = useMemo(() => {
    const high = filteredViolations.filter((item) => item.severity === 'HIGH').length;
    const medium = filteredViolations.filter((item) => item.severity === 'MEDIUM').length;
    const low = filteredViolations.filter((item) => item.severity === 'LOW').length;

    return {
      total: filteredViolations.length,
      high,
      medium,
      low,
    };
  }, [filteredViolations]);

  function applyQueryParams(search) {
    const params = new URLSearchParams(search);
    const className = params.get('class') || '';
    const severity = (params.get('severity') || '').toUpperCase();
    const type = (params.get('type') || '').toUpperCase();
    const focusId = params.get('focus') || '';

    setFilters((prev) => ({
      ...prev,
      className,
      severity: severity && ['HIGH', 'MEDIUM', 'LOW'].includes(severity) ? severity : 'All',
      type: type && ['LAYER_VIOLATION', 'CYCLIC_DEPENDENCY', 'GOD_CLASS', 'HIGH_COUPLING'].includes(type) ? type : 'All',
    }));

    if (focusId) {
      setSelectedViolationId(focusId);
      return;
    }

    setSelectedViolationId(null);
  }

  function clearFilters() {
    setFilters(defaultFilters);
    setSelectedViolationId(null);
    setSeveritySortDirection('desc');
  }

  return {
    loading,
    error,
    refetch,
    filters,
    setFilters,
    clearFilters,
    violations,
    filteredViolations,
    selectedViolation,
    selectedViolationId,
    setSelectedViolationId,
    severitySortDirection,
    toggleSeveritySortDirection: () =>
      setSeveritySortDirection((prev) => (prev === 'desc' ? 'asc' : 'desc')),
    applyQueryParams,
    summary,
  };
}
