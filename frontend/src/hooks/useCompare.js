import { useCallback, useEffect, useMemo, useState } from 'react';
import compareApi from '../api/compareApi';

export default function useCompare(projectId) {
  const [versions, setVersions] = useState([]);
  const [selectedVersions, setSelectedVersions] = useState({ base: '', target: '' });
  const [comparisonData, setComparisonData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const loadVersions = useCallback(async () => {
    if (!projectId) {
      setVersions([]);
      return;
    }

    try {
      setError('');
      const response = await compareApi.getVersions(projectId);
      const data = Array.isArray(response) ? response : [];
      setVersions(data);

      if (data.length >= 2) {
        setSelectedVersions((prev) => ({
          base: prev.base || data[0],
          target: prev.target || data[1],
        }));
      }
    } catch (err) {
      setError(err.message || 'Unable to load analysis versions.');
    }
  }, [projectId]);

  useEffect(() => {
    loadVersions();
  }, [loadVersions]);

  const executeCompare = useCallback(async (overrideBase, overrideTarget) => {
    const base = overrideBase || selectedVersions.base;
    const target = overrideTarget || selectedVersions.target;

    if (!base || !target) {
      return;
    }

    try {
      setLoading(true);
      setError('');
      const response = await compareApi.compare(base, target);
      setComparisonData(response);
    } catch (err) {
      setError(err.message || 'Unable to compare analysis versions.');
    } finally {
      setLoading(false);
    }
  }, [selectedVersions.base, selectedVersions.target]);

  const applyQueryParams = useCallback((search) => {
    const params = new URLSearchParams(search);
    const base = params.get('base') || '';
    const target = params.get('target') || '';

    if (base || target) {
      setSelectedVersions((prev) => ({
        base: base || prev.base,
        target: target || prev.target,
      }));
    }

    return {
      base,
      target,
    };
  }, []);

  const riskDistribution = useMemo(() => {
    const rows = comparisonData?.metricDifferences || [];

    const before = { LOW: 0, MEDIUM: 0, HIGH: 0 };
    const after = { LOW: 0, MEDIUM: 0, HIGH: 0 };

    rows.forEach((row) => {
      if (before[row.riskBefore] !== undefined) {
        before[row.riskBefore] += 1;
      }
      if (after[row.riskAfter] !== undefined) {
        after[row.riskAfter] += 1;
      }
    });

    return { before, after };
  }, [comparisonData]);

  return {
    versions,
    selectedVersions,
    setSelectedVersions,
    comparisonData,
    loading,
    error,
    executeCompare,
    applyQueryParams,
    riskDistribution,
  };
}
