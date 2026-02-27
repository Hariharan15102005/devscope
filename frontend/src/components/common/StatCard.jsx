export default function StatCard({ label, value, icon, tone = 'green', tooltip, onClick }) {
  const interactiveProps = onClick
    ? {
        role: 'button',
        tabIndex: 0,
        onClick,
        onKeyDown: (event) => {
          if (event.key === 'Enter' || event.key === ' ') {
            event.preventDefault();
            onClick();
          }
        },
      }
    : {};

  return (
    <article className={`ds-card ds-stat-card ds-tone-${tone} ${onClick ? 'ds-stat-card-clickable' : ''}`} {...interactiveProps}>
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
