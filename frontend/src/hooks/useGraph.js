import { useCallback, useEffect, useMemo, useState } from 'react';
import graphApi from '../api/graphApi';

const defaultFilters = {
  layer: 'All',
  showCyclesOnly: false,
};

export default function useGraph(projectId) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [graph, setGraph] = useState({ nodes: [], edges: [] });
  const [selectedNodeId, setSelectedNodeId] = useState(null);
  const [selectedNodeDetail, setSelectedNodeDetail] = useState(null);
  const [loadingNodeDetail, setLoadingNodeDetail] = useState(false);
  const [filters, setFilters] = useState(defaultFilters);
  const [highlightedCycles, setHighlightedCycles] = useState(false);

  const refetch = useCallback(async () => {
    if (!projectId) {
      setGraph({ nodes: [], edges: [] });
      return;
    }

    try {
      setLoading(true);
      setError('');
      const response = await graphApi.getGraph(projectId);
      setGraph({
        nodes: response.nodes || [],
        edges: response.edges || [],
      });
    } catch (err) {
      setError(err.message || 'Unable to load dependency graph.');
    } finally {
      setLoading(false);
    }
  }, [projectId]);

  useEffect(() => {
    refetch();
  }, [refetch]);

  useEffect(() => {
    let active = true;

    async function loadDetail() {
      if (!projectId || !selectedNodeId) {
        setSelectedNodeDetail(null);
        return;
      }

      try {
        setLoadingNodeDetail(true);
        const detail = await graphApi.getNodeDetail(projectId, selectedNodeId);
        if (active) {
          setSelectedNodeDetail(detail);
        }
      } catch {
        if (active) {
          setSelectedNodeDetail(null);
        }
      } finally {
        if (active) {
          setLoadingNodeDetail(false);
        }
      }
    }

    loadDetail();

    return () => {
      active = false;
    };
  }, [projectId, selectedNodeId]);

  const layerOptions = useMemo(() => {
    const distinct = new Set(graph.nodes.map((node) => node.layer).filter(Boolean));
    return ['All', ...Array.from(distinct)];
  }, [graph.nodes]);

  return {
    loading,
    error,
    nodes: graph.nodes,
    edges: graph.edges,
    refetch,
    selectedNodeId,
    setSelectedNodeId,
    selectedNodeDetail,
    loadingNodeDetail,
    filters,
    setFilters,
    highlightedCycles,
    setHighlightedCycles,
    layerOptions,
  };
}
