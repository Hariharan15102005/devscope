export default function StatCard({ label, value, icon, tone = 'green', tooltip }) {
  return (
    <article className={`ds-card ds-stat-card ds-tone-${tone}`}>
      <header className="ds-stat-header">
        <h3 className="ds-stat-label">{label}</h3>
        <span className="ds-stat-icon" title={tooltip || label} aria-label={tooltip || label}>
          {icon}
        </span>
      </header>
      <p className="ds-stat-value">{value}</p>
    </article>
  );
}
