export default function SeverityBadge({ severity, mode = 'violation' }) {
  const normalized = (severity || 'LOW').toUpperCase();
  const tone = normalized === 'HIGH' ? 'high' : normalized === 'MEDIUM' ? 'medium' : mode === 'impact' ? 'low-impact' : 'low';

  return <span className={`ds-severity-badge ds-severity-${tone}`}>{normalized}</span>;
}
