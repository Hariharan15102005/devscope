import { useEffect, useMemo, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import EmptyState from '../components/common/EmptyState';
import ErrorBox from '../components/common/ErrorBox';
import Loader from '../components/common/Loader';
import DependencyGraph from '../components/graph/DependencyGraph';
import GraphSidePanel from '../components/graph/GraphSidePanel';
import GraphToolbar from '../components/graph/GraphToolbar';
import useGraph from '../hooks/useGraph';
import MainLayout from '../layouts/MainLayout';
import { useProjectContext } from '../store/ProjectContext';

function resolveNodeByQuery(nodes, queryValue) {
  if (!queryValue) {
    return null;
  }

  const normalized = queryValue.trim().toLowerCase();
  return (
    nodes.find((item) => item.fullName.toLowerCase() === normalized) ||
    nodes.find((item) => item.name.toLowerCase() === normalized) ||
    null
  );
}

export default function DependencyGraphPage() {
  const { projectId } = useProjectContext();
  const location = useLocation();
  const graphRef = useRef(null);

  const {
    loading,
    error,
    nodes,
    edges,
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
  } = useGraph(projectId);

  const selectedNode = useMemo(() => {
    return nodes.find((node) => node.id === selectedNodeId) || null;
  }, [nodes, selectedNodeId]);

  useEffect(() => {
    if (!nodes.length) {
      return;
    }

    const params = new URLSearchParams(location.search);
    const focusNode = params.get('focus');
    const highlight = params.get('highlight');
    const layer = params.get('layer');

    // Navigation integration: react to URL query changes from Dashboard/Structure/Metrics/Violations/Insights.
    if (layer) {
      setFilters((prev) => ({ ...prev, layer }));
    }

    if (highlight === 'cycles') {
      setHighlightedCycles(true);
      setFilters((prev) => ({ ...prev, showCyclesOnly: true }));

      const firstCycleEdge = edges.find((edge) => edge.cycle);
      if (firstCycleEdge) {
        setSelectedNodeId(firstCycleEdge.source);
        graphRef.current?.focusNode(firstCycleEdge.source);
      }
    }

    if (focusNode) {
      const resolvedNode = resolveNodeByQuery(nodes, focusNode);
      if (resolvedNode) {
        setSelectedNodeId(resolvedNode.id);
        graphRef.current?.expandLayout();
        graphRef.current?.focusNode(resolvedNode.id);
      }
    }
  }, [location.search, nodes, edges, setFilters, setHighlightedCycles, setSelectedNodeId]);

  const breadcrumb = selectedNode
    ? `Dashboard > Dependency Graph > ${selectedNode.name}`
    : 'Dashboard > Dependency Graph';

  function handleLayerChange(layer) {
    setFilters((prev) => ({ ...prev, layer }));
  }

  function handleToggleCyclesOnly(showCyclesOnly) {
    setFilters((prev) => ({ ...prev, showCyclesOnly }));
    setHighlightedCycles(showCyclesOnly);
  }

  function handleClearSelection() {
    setSelectedNodeId(null);
  }

  if (loading) {
    return (
      <MainLayout title="Dependency Graph">
        <Loader label="Loading dependency graph..." />
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout title="Dependency Graph">
        <ErrorBox message={error} onRetry={refetch} />
      </MainLayout>
    );
  }

  if (!nodes.length) {
    return (
      <MainLayout title="Dependency Graph">
        <section className="ds-breadcrumb">Dashboard &gt; Dependency Graph</section>
        <EmptyState title="No graph data found." />
      </MainLayout>
    );
  }

  return (
    <MainLayout title="Dependency Graph">
      <section className="ds-breadcrumb">{breadcrumb}</section>

      <GraphToolbar
        layerOptions={layerOptions}
        filters={filters}
        onLayerChange={handleLayerChange}
        onToggleCyclesOnly={handleToggleCyclesOnly}
        onResetLayout={() => graphRef.current?.resetLayout()}
        onCenter={() => graphRef.current?.centerGraph()}
        onClearSelection={handleClearSelection}
      />

      <section className="ds-graph-layout">
        <DependencyGraph
          ref={graphRef}
          nodes={nodes}
          edges={edges}
          selectedNodeId={selectedNodeId}
          filters={filters}
          highlightedCycles={highlightedCycles}
          onNodeSelect={setSelectedNodeId}
        />

        <GraphSidePanel isOpen={Boolean(selectedNodeId)} detail={selectedNodeDetail} loading={loadingNodeDetail} />
      </section>
    </MainLayout>
  );
}
