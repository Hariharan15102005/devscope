import { useEffect, useRef } from 'react';
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

export default function DependencyGraphPage() {
  const { analysisId } = useProjectContext();
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
    selectedNode,
    selectedNodeDetail,
    filters,
    setFilters,
    highlightedCycles,
    setHighlightedCycles,
    layerOptions,
    applyQueryParams,
  } = useGraph(analysisId);

  useEffect(() => {
    if (!nodes.length) {
      return;
    }

    // URL-driven cross-navigation integration from Dashboard/Structure/Metrics/Violations/Insights.
    const queryIntent = applyQueryParams(location.search);
    if (queryIntent.focusNodeId) {
      graphRef.current?.expandLayout();
      graphRef.current?.focusNode(queryIntent.focusNodeId);
    }

    if (queryIntent.shouldShowCyclesOnly) {
      const cycleEdge = edges.find((edge) => edge.isCycle);
      if (cycleEdge) {
        setSelectedNodeId(cycleEdge.source);
        graphRef.current?.focusNode(cycleEdge.source);
      }
    }
  }, [location.search, nodes, edges, applyQueryParams, setSelectedNodeId]);

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
        onFitToScreen={() => graphRef.current?.fitToScreen()}
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

        <GraphSidePanel isOpen={Boolean(selectedNodeId)} detail={selectedNodeDetail} loading={false} />
      </section>
    </MainLayout>
  );
}
