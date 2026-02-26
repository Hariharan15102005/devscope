export default function BarChart({ data }) {
  const chartData = data || [];
  const maxValue = Math.max(...chartData.map((item) => item.value), 1);

  return (
    <div className="ds-bars" aria-label="Complexity distribution chart">
      {chartData.map((item) => (
        <div className="ds-bar-row" key={item.label}>
          <span className="ds-bar-label">{item.label}</span>
          <div className="ds-bar-track">
            <div className="ds-bar-fill" style={{ width: `${(item.value / maxValue) * 100}%` }} />
          </div>
          <strong className="ds-bar-value">{item.value}</strong>
        </div>
      ))}
    </div>
  );
}
