import SeverityBadge from '../common/SeverityBadge';

export default function InsightCard({ insight, selected, onSelect }) {
  return (
    <article
      className={`ds-card ds-insight-card ${selected ? 'is-selected' : ''}`}
      role="button"
      tabIndex={0}
      onClick={() => onSelect(insight.id)}
      onKeyDown={(event) => {
        if (event.key === 'Enter' || event.key === ' ') {
          event.preventDefault();
          onSelect(insight.id);
        }
      }}
    >
      <div className="ds-insight-card-head">
        <h3>{insight.title}</h3>
        <SeverityBadge severity={insight.impactLevel} mode="impact" />
      </div>

      <p className="ds-insight-card-category">{insight.category}</p>
      <p>{insight.description}</p>
      <p className="ds-insight-card-foot">Affected classes: {(insight.affectedClasses || []).length}</p>
    </article>
  );
}
