import { forwardRef, useEffect, useImperativeHandle, useMemo, useRef, useState } from 'react';
import ReactFlow, { Background, Controls, MarkerType, ReactFlowProvider } from 'reactflow';
import 'reactflow/dist/style.css';
import GraphLegend from './GraphLegend';
import GraphMiniMap from './GraphMiniMap';

function toDisplayType(type) {
  return type.charAt(0) + type.slice(1).toLowerCase();
}

function toTone(type) {
  if (type === 'CONTROLLER') return 'controller';
  if (type === 'SERVICE') return 'service';
  if (type === 'REPOSITORY') return 'repository';
  return 'other';
}

function buildRadialPositions(inputNodes, spread = 260) {
  if (!inputNodes.length) {
    return {};
  }

  const angleStep = (Math.PI * 2) / inputNodes.length;

  return inputNodes.reduce((acc, node, index) => {
    const angle = index * angleStep;
    acc[node.id] = {
      x: Math.cos(angle) * spread + 380,
      y: Math.sin(angle) * spread + 300,
    };
    return acc;
  }, {});
}

const DependencyGraph = forwardRef(function DependencyGraph(
  { nodes, edges, selectedNodeId, filters, highlightedCycles, onNodeSelect },
  ref
) {
  const [hoverNodeId, setHoverNodeId] = useState(null);
  const [positionMap, setPositionMap] = useState(() => buildRadialPositions(nodes));
  const graphInstanceRef = useRef(null);

  useEffect(() => {
    setPositionMap(buildRadialPositions(nodes));
  }, [nodes]);

  const visibleNodeIds = useMemo(() => {
    let filteredNodes = nodes;

    if (filters.layer !== 'All') {
      filteredNodes = filteredNodes.filter(
        (node) => toDisplayType(node.type).toLowerCase() === filters.layer.toLowerCase()
      );
    }

    if (filters.showCyclesOnly) {
      const cycleNodeIds = new Set(
        edges.filter((edge) => edge.isCycle).flatMap((edge) => [edge.source, edge.target])
      );
      filteredNodes = filteredNodes.filter((node) => cycleNodeIds.has(node.id));
    }

    return new Set(filteredNodes.map((node) => node.id));
  }, [nodes, edges, filters.layer, filters.showCyclesOnly]);

  const flowNodes = useMemo(() => {
    return nodes
      .filter((node) => visibleNodeIds.has(node.id))
      .map((node) => {
        const isSelected = selectedNodeId === node.id;
        const isHovered = hoverNodeId === node.id;

        return {
          id: node.id,
          position: positionMap[node.id] || { x: 0, y: 0 },
          draggable: true,
          data: {
            label: (
              <div className="ds-rf-node-content">
                <strong>{node.name}</strong>
                <small>{toDisplayType(node.type)}</small>
              </div>
            ),
            type: node.type,
            isHighCoupling: node.isHighCoupling,
          },
          className: [
            'ds-rf-node',
            `tone-${toTone(node.type)}`,
            node.isHighCoupling ? 'is-high-coupling' : '',
            isSelected ? 'is-selected' : '',
            isHovered ? 'is-hovered' : '',
          ]
            .filter(Boolean)
            .join(' '),
        };
      });
  }, [nodes, visibleNodeIds, positionMap, selectedNodeId, hoverNodeId]);

  const flowEdges = useMemo(() => {
    return edges
      .filter((edge) => visibleNodeIds.has(edge.source) && visibleNodeIds.has(edge.target))
      .filter((edge) => (filters.showCyclesOnly ? edge.isCycle : true))
      .map((edge) => ({
        id: `${edge.source}-${edge.target}`,
        source: edge.source,
        target: edge.target,
        animated: false,
        markerEnd: { type: MarkerType.ArrowClosed, width: 18, height: 18 },
        className: edge.isCycle && highlightedCycles ? 'is-cycle' : 'is-normal',
        style: {
          stroke: edge.isCycle && highlightedCycles ? '#ef4444' : '#94a3b8',
          strokeWidth: edge.isCycle && highlightedCycles ? 2.6 : 1.2,
        },
        data: {
          isCycle: edge.isCycle,
        },
      }));
  }, [edges, visibleNodeIds, filters.showCyclesOnly, highlightedCycles]);

  useEffect(() => {
    if (!selectedNodeId || !graphInstanceRef.current) {
      return;
    }

    const selected = flowNodes.find((node) => node.id === selectedNodeId);
    if (!selected) {
      return;
    }

    graphInstanceRef.current.setCenter(selected.position.x, selected.position.y, {
      zoom: 1.25,
      duration: 450,
    });
  }, [selectedNodeId, flowNodes]);

  useImperativeHandle(ref, () => ({
    resetLayout() {
      setPositionMap(buildRadialPositions(nodes));
      graphInstanceRef.current?.fitView({ padding: 0.2, duration: 500 });
    },
    fitToScreen() {
      graphInstanceRef.current?.fitView({ padding: 0.18, duration: 500 });
    },
    focusNode(nodeId) {
      const selected = flowNodes.find((node) => node.id === nodeId);
      if (!selected) {
        return;
      }

      graphInstanceRef.current?.setCenter(selected.position.x, selected.position.y, {
        zoom: 1.4,
        duration: 450,
      });
    },
    expandLayout() {
      setPositionMap(buildRadialPositions(nodes, 340));
      graphInstanceRef.current?.fitView({ padding: 0.15, duration: 500 });
    },
  }));

  return (
    <section className="ds-card ds-graph-canvas-wrap">
      <ReactFlowProvider>
        <div className="ds-reactflow-host">
          <ReactFlow
            nodes={flowNodes}
            edges={flowEdges}
            onInit={(instance) => {
              graphInstanceRef.current = instance;
              instance.fitView({ padding: 0.18 });
            }}
            onNodeClick={(_, node) => onNodeSelect(node.id)}
            onNodeMouseEnter={(_, node) => setHoverNodeId(node.id)}
            onNodeMouseLeave={() => setHoverNodeId(null)}
            onNodeDragStop={(_, node) => {
              setPositionMap((prev) => ({ ...prev, [node.id]: node.position }));
            }}
            fitView
            minZoom={0.35}
            maxZoom={2.5}
            proOptions={{ hideAttribution: true }}
          >
            <Background gap={18} size={1} color="#e2e8f0" />
            <Controls showInteractive={false} position="top-left" />
            <GraphMiniMap />
          </ReactFlow>
        </div>
      </ReactFlowProvider>

      <GraphLegend />
    </section>
  );
});

export default DependencyGraph;
