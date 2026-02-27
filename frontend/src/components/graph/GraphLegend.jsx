export default function GraphLegend() {
  return (
    <div className="ds-graph-legend">
      <div className="ds-graph-legend-group">
        <span className="ds-graph-legend-title">Nodes</span>
        <span className="ds-graph-legend-item">
          <i className="swatch tone-controller" /> Controller
        </span>
        <span className="ds-graph-legend-item">
          <i className="swatch tone-service" /> Service
        </span>
        <span className="ds-graph-legend-item">
          <i className="swatch tone-repository" /> Repository
        </span>
        <span className="ds-graph-legend-item">
          <i className="swatch tone-other" /> Other
        </span>
      </div>

      <div className="ds-graph-legend-group">
        <span className="ds-graph-legend-title">Edges</span>
        <span className="ds-graph-legend-item">
          <i className="edge normal" /> Normal dependency
        </span>
        <span className="ds-graph-legend-item">
          <i className="edge cycle" /> Cyclic dependency
        </span>
      </div>
    </div>
  );
}
