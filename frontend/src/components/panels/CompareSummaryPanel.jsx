function getTrendTone(value, inverse = false) {
  const improved = inverse ? value < 0 : value > 0;
  if (value === 0) return 'neutral';
  return improved ? 'better' : 'worse';
}

function TrendItem({ label, value, inverse = false, suffix = '' }) {
  const tone = getTrendTone(value, inverse);
  const icon = tone === 'better' ? '▲' : tone === 'worse' ? '▼' : '•';
  const text = value > 0 ? `+${value}` : `${value}`;

  return (
    <article className={`ds-card ds-compare-summary-item tone-${tone}`}>
      <h3>{label}</h3>
      <p>
        <span>{icon}</span> {text}
        {suffix}
      </p>
    </article>
  );
}

export default function CompareSummaryPanel({ summary }) {
  if (!summary) {
    return null;
  }

  return (
    <section className="ds-grid ds-compare-summary-grid">
      <TrendItem label="Violation Change" value={summary.violationChange} inverse />
      <TrendItem label="Avg Complexity Change" value={summary.avgComplexityChange} inverse />
      <TrendItem label="Cycle Count Change" value={summary.cycleChange} inverse />
      <TrendItem label="Overall Risk Trend" value={summary.riskChange} inverse />
    </section>
  );
}
