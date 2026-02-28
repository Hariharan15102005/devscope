import { useCallback, useEffect, useMemo, useState } from 'react';
import graphApi from '../api/graphApi';

const defaultFilters = {
  layer: 'All',
  showCyclesOnly: false,
};

export default function useGraph(analysisId) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [graph, setGraph] = useState({ nodes: [], edges: [] });
  const [selectedNodeId, setSelectedNodeId] = useState(null);
  const [filters, setFilters] = useState(defaultFilters);
  const [highlightedCycles, setHighlightedCycles] = useState(false);

  const refetch = useCallback(async () => {
    if (!analysisId) {
      setGraph({ nodes: [], edges: [] });
      return;
    }

    try {
      setLoading(true);
      setError('');
      const response = await graphApi.getGraph(analysisId);
      setGraph({
        nodes: response.nodes || [],
        edges: response.edges || [],
      });
    } catch (err) {
      setError(err.message || 'Unable to load dependency graph.');
    } finally {
      setLoading(false);
    }
  }, [analysisId]);

  useEffect(() => {
    refetch();
  }, [refetch]);

  const layerOptions = useMemo(() => {
    const distinct = new Set(
      graph.nodes
        .map((node) => node.type)
        .filter(Boolean)
        .map((value) => value.charAt(0) + value.slice(1).toLowerCase())
    );
    return ['All', ...Array.from(distinct)];
  }, [graph.nodes]);

  const nodeById = useMemo(() => {
    return new Map(graph.nodes.map((node) => [node.id, node]));
  }, [graph.nodes]);

  const selectedNode = useMemo(() => {
    return graph.nodes.find((node) => node.id === selectedNodeId) || null;
  }, [graph.nodes, selectedNodeId]);

  const selectedNodeDetail = useMemo(() => {
    if (!selectedNode) {
      return null;
    }

    const outgoingDependencies = graph.edges
      .filter((edge) => edge.source === selectedNode.id)
      .map((edge) => nodeById.get(edge.target))
      .filter(Boolean)
      .map((node) => node.name);

    const incomingDependents = graph.edges
      .filter((edge) => edge.target === selectedNode.id)
      .map((edge) => nodeById.get(edge.source))
      .filter(Boolean)
      .map((node) => node.name);

    return {
      id: selectedNode.id,
      name: selectedNode.name,
      fullName: selectedNode.id,
      type: selectedNode.type,
      dependencyCount: selectedNode.dependencyCount,
      dependentCount: selectedNode.dependentCount,
      outgoingDependencies,
      incomingDependents,
    };
  }, [graph.edges, nodeById, selectedNode]);

  const focusNodeByQuery = useCallback(
    (queryValue) => {
      if (!queryValue) {
        return null;
      }

      const normalized = queryValue.trim().toLowerCase();
      const node =
        graph.nodes.find((item) => item.id.toLowerCase() === normalized) ||
        graph.nodes.find((item) => item.name.toLowerCase() === normalized) ||
        null;

      if (node) {
        setSelectedNodeId(node.id);
      }

      return node;
    },
    [graph.nodes]
  );

  const applyQueryParams = useCallback(
    (search) => {
      const params = new URLSearchParams(search);
      const focus = params.get('focus');
      const highlight = params.get('highlight');
      const layer = params.get('layer');

      if (layer) {
        const formattedLayer = layer.charAt(0).toUpperCase() + layer.slice(1).toLowerCase();
        setFilters((prev) => ({ ...prev, layer: formattedLayer }));
      }

      if (highlight === 'cycles') {
        setHighlightedCycles(true);
        setFilters((prev) => ({ ...prev, showCyclesOnly: true }));
      }

      const focusedNode = focusNodeByQuery(focus);
      if (!focus && highlight !== 'cycles') {
        setHighlightedCycles(false);
      }

      return {
        focusNodeId: focusedNode?.id || null,
        shouldShowCyclesOnly: highlight === 'cycles',
      };
    },
    [focusNodeByQuery]
  );

  return {
    loading,
    error,
    nodes: graph.nodes,
    edges: graph.edges,
    refetch,
    selectedNodeId,
    setSelectedNodeId,
    selectedNode,
    selectedNodeDetail,
    filters,
    setFilters,
    highlightedCycles,
    setHighlightedCycles,
    layerOptions,
    applyQueryParams,
  };
}
