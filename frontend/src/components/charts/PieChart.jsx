const PIE_COLORS = ['#2563eb', '#22c55e', '#f59e0b', '#ef4444'];

function buildGradient(data) {
  const total = data.reduce((sum, item) => sum + item.value, 0);
  if (!total) return 'conic-gradient(#e2e8f0 0deg 360deg)';

  let currentDeg = 0;
  const segments = data.map((item, index) => {
    const sweep = (item.value / total) * 360;
    const start = currentDeg;
    const end = currentDeg + sweep;
    currentDeg = end;
    return `${PIE_COLORS[index % PIE_COLORS.length]} ${start}deg ${end}deg`;
  });

  return `conic-gradient(${segments.join(', ')})`;
}

export default function PieChart({ data }) {
  const chartData = data || [];
  const total = chartData.reduce((sum, item) => sum + item.value, 0);

  return (
    <div className="ds-pie-grid">
      <div className="ds-pie" style={{ background: buildGradient(chartData) }} aria-label="Layer distribution chart" />
      <ul className="ds-legend">
        {chartData.map((item, index) => (
          <li key={item.label}>
            <span className="ds-dot" style={{ backgroundColor: PIE_COLORS[index % PIE_COLORS.length] }} />
            <span>{item.label}</span>
            <strong>{item.value}</strong>
          </li>
        ))}
        <li className="ds-total-row">
          <span>Total</span>
          <strong>{total}</strong>
        </li>
      </ul>
    </div>
  );
}
