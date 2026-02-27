import { forwardRef, useEffect, useImperativeHandle, useMemo, useState } from 'react';

const NODE_WIDTH = 170;
const NODE_HEIGHT = 52;

function getLayerTone(layer) {
  if (layer === 'Controller') return 'controller';
  if (layer === 'Service') return 'service';
  if (layer === 'Repository') return 'repository';
  return 'other';
}

function buildLayout(nodes, expanded = false) {
  if (!nodes.length) {
    return {};
  }

  const radius = expanded ? 340 : 260;
  const angleStep = (2 * Math.PI) / nodes.length;
  const centerX = 0;
  const centerY = 0;

  return nodes.reduce((acc, node, index) => {
    const angle = index * angleStep;
    acc[node.id] = {
      x: centerX + Math.cos(angle) * radius,
      y: centerY + Math.sin(angle) * radius,
    };
    return acc;
  }, {});
}

const DependencyGraph = forwardRef(function DependencyGraph(
  { nodes, edges, selectedNodeId, filters, highlightedCycles, onNodeSelect },
  ref
) {
  const [positions, setPositions] = useState(() => buildLayout(nodes));
  const [viewport, setViewport] = useState({ x: 520, y: 360, scale: 1 });
  const [draggingNodeId, setDraggingNodeId] = useState(null);
  const [hoverNodeId, setHoverNodeId] = useState(null);
  const [panning, setPanning] = useState(false);

  useEffect(() => {
    setPositions(buildLayout(nodes));
  }, [nodes]);

  const nodeById = useMemo(() => {
    return new Map(nodes.map((node) => [node.id, node]));
  }, [nodes]);

  const visibleNodeSet = useMemo(() => {
    let filteredNodes = nodes;

    if (filters.layer !== 'All') {
      filteredNodes = filteredNodes.filter((node) => node.layer === filters.layer);
    }

    if (filters.showCyclesOnly) {
      const cycleNodeIds = new Set(
        edges.filter((edge) => edge.cycle).flatMap((edge) => [edge.source, edge.target])
      );
      filteredNodes = filteredNodes.filter((node) => cycleNodeIds.has(node.id));
    }

    return new Set(filteredNodes.map((node) => node.id));
  }, [nodes, edges, filters.layer, filters.showCyclesOnly]);

  const visibleNodes = useMemo(() => {
    return nodes.filter((node) => visibleNodeSet.has(node.id));
  }, [nodes, visibleNodeSet]);

  const visibleEdges = useMemo(() => {
    return edges.filter((edge) => {
      if (!visibleNodeSet.has(edge.source) || !visibleNodeSet.has(edge.target)) {
        return false;
      }

      if (filters.showCyclesOnly) {
        return edge.cycle;
      }

      return true;
    });
  }, [edges, visibleNodeSet, filters.showCyclesOnly]);

  useImperativeHandle(ref, () => ({
    resetLayout() {
      setPositions(buildLayout(nodes));
      setViewport({ x: 520, y: 360, scale: 1 });
    },
    centerGraph() {
      setViewport((prev) => ({ ...prev, x: 520, y: 360 }));
    },
    expandLayout() {
      setPositions(buildLayout(nodes, true));
    },
    focusNode(nodeId) {
      const point = positions[nodeId];
      if (!point) {
        return;
      }
      setViewport((prev) => ({
        ...prev,
        x: 520 - point.x * prev.scale,
        y: 360 - point.y * prev.scale,
      }));
    },
  }));

  function handleWheel(event) {
    event.preventDefault();
    const nextScale = Math.min(2.5, Math.max(0.55, viewport.scale + (event.deltaY < 0 ? 0.08 : -0.08)));
    setViewport((prev) => ({ ...prev, scale: nextScale }));
  }

  function handleBackgroundPointerDown(event) {
    if (event.target.dataset.role !== 'graph-canvas') {
      return;
    }

    setPanning(true);
  }

  function handleNodePointerDown(event, nodeId) {
    event.stopPropagation();
    setDraggingNodeId(nodeId);
  }

  function handlePointerMove(event) {
    if (draggingNodeId) {
      const deltaX = event.movementX / viewport.scale;
      const deltaY = event.movementY / viewport.scale;
      setPositions((prev) => {
        const point = prev[draggingNodeId];
        if (!point) {
          return prev;
        }

        return {
          ...prev,
          [draggingNodeId]: { x: point.x + deltaX, y: point.y + deltaY },
        };
      });
      return;
    }

    if (panning) {
      setViewport((prev) => ({
        ...prev,
        x: prev.x + event.movementX,
        y: prev.y + event.movementY,
      }));
    }
  }

  function stopAllDragging() {
    if (draggingNodeId) {
      setDraggingNodeId(null);
    }
    if (panning) {
      setPanning(false);
    }
  }

  const bounds = useMemo(() => {
    const points = Object.values(positions);
    if (!points.length) {
      return { minX: -1, minY: -1, width: 2, height: 2 };
    }

    const xs = points.map((item) => item.x);
    const ys = points.map((item) => item.y);
    const minX = Math.min(...xs) - NODE_WIDTH;
    const maxX = Math.max(...xs) + NODE_WIDTH;
    const minY = Math.min(...ys) - NODE_HEIGHT;
    const maxY = Math.max(...ys) + NODE_HEIGHT;

    return { minX, minY, width: maxX - minX, height: maxY - minY };
  }, [positions]);

  return (
    <section className="ds-card ds-graph-canvas-wrap">
      <svg
        className="ds-graph-canvas"
        viewBox="0 0 1040 720"
        onWheel={handleWheel}
        onPointerMove={handlePointerMove}
        onPointerUp={stopAllDragging}
        onPointerLeave={stopAllDragging}
        onPointerDown={handleBackgroundPointerDown}
      >
        <rect data-role="graph-canvas" x="0" y="0" width="1040" height="720" fill="transparent" />
        <g transform={`translate(${viewport.x}, ${viewport.y}) scale(${viewport.scale})`}>
          {visibleEdges.map((edge, index) => {
            const source = positions[edge.source];
            const target = positions[edge.target];
            if (!source || !target) {
              return null;
            }

            const isCycleEdge = edge.cycle && highlightedCycles;

            return (
              <line
                key={`${edge.source}-${edge.target}-${index}`}
                x1={source.x}
                y1={source.y}
                x2={target.x}
                y2={target.y}
                className={`ds-graph-edge ${isCycleEdge ? 'is-cycle' : ''}`}
              />
            );
          })}

          {visibleNodes.map((node) => {
            const point = positions[node.id];
            if (!point) {
              return null;
            }

            const isSelected = selectedNodeId === node.id;
            const isHovered = hoverNodeId === node.id;

            return (
              <g
                key={node.id}
                className={`ds-graph-node ${isSelected ? 'is-selected' : ''} ${isHovered ? 'is-hovered' : ''}`}
                transform={`translate(${point.x - NODE_WIDTH / 2}, ${point.y - NODE_HEIGHT / 2})`}
                onPointerEnter={() => setHoverNodeId(node.id)}
                onPointerLeave={() => setHoverNodeId(null)}
                onPointerDown={(event) => handleNodePointerDown(event, node.id)}
                onClick={() => onNodeSelect(node.id)}
              >
                <rect width={NODE_WIDTH} height={NODE_HEIGHT} rx="12" className={`ds-graph-node-box tone-${getLayerTone(node.layer)}`} />
                <text x="12" y="22" className="ds-graph-node-title">
                  {node.name}
                </text>
                <text x="12" y="39" className="ds-graph-node-subtitle">
                  {node.layer}
                </text>
              </g>
            );
          })}
        </g>
      </svg>

      <div className="ds-graph-minimap-wrap">
        <svg className="ds-graph-minimap" viewBox={`${bounds.minX} ${bounds.minY} ${bounds.width} ${bounds.height}`}>
          {visibleEdges.map((edge, index) => {
            const source = positions[edge.source];
            const target = positions[edge.target];
            if (!source || !target) {
              return null;
            }

            return (
              <line
                key={`mini-${edge.source}-${edge.target}-${index}`}
                x1={source.x}
                y1={source.y}
                x2={target.x}
                y2={target.y}
                className={`ds-graph-minimap-edge ${edge.cycle && highlightedCycles ? 'is-cycle' : ''}`}
              />
            );
          })}

          {visibleNodes.map((node) => {
            const point = positions[node.id];
            if (!point) {
              return null;
            }

            return (
              <rect
                key={`mini-node-${node.id}`}
                x={point.x - 8}
                y={point.y - 5}
                width="16"
                height="10"
                rx="2"
                className={`ds-graph-minimap-node ${selectedNodeId === node.id ? 'is-selected' : ''}`}
              />
            );
          })}
        </svg>
      </div>
    </section>
  );
});

export default DependencyGraph;
