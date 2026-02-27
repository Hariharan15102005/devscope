export default function GraphToolbar({
  layerOptions,
  filters,
  onLayerChange,
  onToggleCyclesOnly,
  onResetLayout,
  onCenter,
  onClearSelection,
}) {
  return (
    <section className="ds-card ds-graph-toolbar">
      <label className="ds-graph-toolbar-field">
        <span>Layer</span>
        <select value={filters.layer} onChange={(event) => onLayerChange(event.target.value)}>
          {layerOptions.map((layer) => (
            <option key={layer} value={layer}>
              {layer}
            </option>
          ))}
        </select>
      </label>

      <label className="ds-graph-toolbar-toggle">
        <input
          type="checkbox"
          checked={filters.showCyclesOnly}
          onChange={(event) => onToggleCyclesOnly(event.target.checked)}
        />
        <span>Show Cycles Only</span>
      </label>

      <div className="ds-graph-toolbar-actions">
        <button type="button" className="ds-btn" onClick={onResetLayout}>
          Reset Layout
        </button>
        <button type="button" className="ds-btn" onClick={onCenter}>
          Center Graph
        </button>
        <button type="button" className="ds-btn" onClick={onClearSelection}>
          Clear Selection
        </button>
      </div>
    </section>
  );
}
