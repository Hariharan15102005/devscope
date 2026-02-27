import { MiniMap } from 'reactflow';

function getColor(node) {
  if (node?.data?.type === 'CONTROLLER') return '#2563eb';
  if (node?.data?.type === 'SERVICE') return '#7c3aed';
  if (node?.data?.type === 'REPOSITORY') return '#16a34a';
  return '#64748b';
}

export default function GraphMiniMap() {
  return (
    <MiniMap
      pannable
      zoomable
      className="ds-reactflow-minimap"
      nodeColor={getColor}
      nodeStrokeWidth={2}
      maskColor="rgba(15, 23, 42, 0.08)"
    />
  );
}
