export default function ComparisonBarChart({ title, rows }) {
  const max = Math.max(
    1,
    ...rows.flatMap((row) => [Math.abs(row.before), Math.abs(row.after)])
  );

  return (
    <section className="ds-card ds-compare-chart">
      <h3>{title}</h3>
      <div className="ds-compare-bars">
        {rows.map((row) => (
          <div key={row.label} className="ds-compare-bar-row">
            <span className="ds-compare-bar-label">{row.label}</span>
            <div className="ds-compare-bar-group">
              <div className="ds-compare-bar-track" title={`Before: ${row.before}`}>
                <div className="ds-compare-bar before" style={{ width: `${(Math.abs(row.before) / max) * 100}%` }} />
              </div>
              <div className="ds-compare-bar-track" title={`After: ${row.after}`}>
                <div className="ds-compare-bar after" style={{ width: `${(Math.abs(row.after) / max) * 100}%` }} />
              </div>
            </div>
            <span className="ds-compare-bar-values">{row.before} → {row.after}</span>
          </div>
        ))}
      </div>
    </section>
  );
}
