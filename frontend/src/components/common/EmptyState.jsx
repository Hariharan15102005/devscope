export default function EmptyState({ title = 'No data available' }) {
  return <div className="ds-empty-state">{title}</div>;
}
