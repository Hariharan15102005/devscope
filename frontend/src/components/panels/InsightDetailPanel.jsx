import { useNavigate } from 'react-router-dom';
import SeverityBadge from '../common/SeverityBadge';

export default function InsightDetailPanel({ insight, isOpen }) {
  const navigate = useNavigate();

  if (!insight) {
    return (
      <aside className={`ds-card ds-insight-detail ${isOpen ? 'is-open' : 'is-collapsed'} ds-insight-detail-empty`}>
        Select a recommendation to inspect reasoning and implementation steps.
      </aside>
    );
  }

  const firstAffectedClass = insight.affectedClasses?.[0] || '';

  return (
    <aside className={`ds-card ds-insight-detail ${isOpen ? 'is-open' : 'is-collapsed'}`}>
      <h3>{insight.title}</h3>

      <div className="ds-insight-detail-meta">
        <SeverityBadge severity={insight.impactLevel} mode="impact" />
        <span>{insight.category}</span>
      </div>

      <section>
        <h4>Description</h4>
        <p>{insight.description}</p>
      </section>

      <section>
        <h4>Root Cause / Reasoning</h4>
        <p>{insight.reasoning}</p>
      </section>

      <section>
        <h4>Affected Classes</h4>
        <ul className="ds-insight-class-list">
          {(insight.affectedClasses || []).map((className) => (
            <li key={className}>
              <button type="button" className="ds-link-btn" onClick={() => navigate(`/structure?class=${encodeURIComponent(className)}`)}>
                {className}
              </button>
            </li>
          ))}
        </ul>
      </section>

      <section>
        <h4>Recommendation Steps</h4>
        <ol className="ds-insight-step-list">
          {(insight.recommendationSteps || []).map((step) => (
            <li key={step}>{step}</li>
          ))}
        </ol>
      </section>

      <div className="ds-insight-actions">
        <button
          type="button"
          className="ds-btn"
          onClick={() => navigate(`/structure?class=${encodeURIComponent(firstAffectedClass)}`)}
        >
          View in Structure
        </button>
        <button
          type="button"
          className="ds-btn"
          onClick={() =>
            insight.category === 'CYCLE'
              ? navigate('/graph?highlight=cycles')
              : navigate(`/graph?focus=${encodeURIComponent(firstAffectedClass)}`)
          }
        >
          View in Dependency Graph
        </button>
        <button
          type="button"
          className="ds-btn"
          onClick={() => navigate(`/metrics?class=${encodeURIComponent(firstAffectedClass)}`)}
        >
          View Metrics
        </button>
        <button
          type="button"
          className="ds-btn"
          onClick={() => navigate(`/violations?class=${encodeURIComponent(firstAffectedClass)}`)}
        >
          View Violations
        </button>
      </div>
    </aside>
  );
}
