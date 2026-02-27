export default function ComparisonLineChart({ title, before, after, yLabel = 'Value' }) {
  const width = 320;
  const height = 160;
  const padding = 24;

  const min = Math.min(before, after);
  const max = Math.max(before, after);
  const domain = Math.max(1, max - min);

  const toY = (value) => height - padding - ((value - min) / domain) * (height - padding * 2);

  const x1 = padding;
  const x2 = width - padding;
  const y1 = toY(before);
  const y2 = toY(after);

  return (
    <section className="ds-card ds-compare-chart">
      <h3>{title}</h3>
      <svg className="ds-compare-line-chart" viewBox={`0 0 ${width} ${height}`} role="img" aria-label={title}>
        <line x1={x1} y1={y1} x2={x2} y2={y2} className="ds-compare-line" />
        <circle cx={x1} cy={y1} r="5" className="ds-compare-point before" />
        <circle cx={x2} cy={y2} r="5" className="ds-compare-point after" />
        <text x={x1 - 6} y={height - 6} className="ds-compare-axis-label">Before</text>
        <text x={x2 - 18} y={height - 6} className="ds-compare-axis-label">After</text>
        <text x={x1 - 18} y={y1 - 8} className="ds-compare-value-label">{before}</text>
        <text x={x2 + 8} y={y2 - 8} className="ds-compare-value-label">{after}</text>
        <text x={width / 2 - 20} y={12} className="ds-compare-axis-label">{yLabel}</text>
      </svg>
    </section>
  );
}
