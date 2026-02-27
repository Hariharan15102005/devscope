import { useNavigate } from 'react-router-dom';
import SeverityBadge from '../common/SeverityBadge';

export default function ViolationDetailPanel({ violation, isOpen = false }) {
  const navigate = useNavigate();

  if (!violation) {
    return (
      <aside className={`ds-card ds-violation-panel ${isOpen ? 'ds-violation-panel-open' : 'ds-violation-panel-collapsed'} ds-violation-panel-empty`}>
        Select a violation to inspect details and navigation actions.
      </aside>
    );
  }

  const firstAffectedClass = violation.affectedClasses?.[0] || '';

  return (
    <aside className={`ds-card ds-violation-panel ${isOpen ? 'ds-violation-panel-open' : 'ds-violation-panel-collapsed'}`}>
      <h3>{violation.title}</h3>
      <div className="ds-violation-panel-meta">
        <SeverityBadge severity={violation.severity} />
        <span className="ds-violation-type">{violation.type}</span>
      </div>

      <section>
        <h4>Description</h4>
        <p>{violation.description}</p>
      </section>

      <section>
        <h4>Affected Classes</h4>
        <ul className="ds-violation-class-list">
          {(violation.affectedClasses || []).map((className) => (
            <li key={className}>{className}</li>
          ))}
        </ul>
      </section>

      <section>
        <h4>Recommended Fix</h4>
        <div className="ds-violation-recommendation">{violation.recommendation}</div>
      </section>

      <div className="ds-violation-actions">
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
            violation.type === 'CYCLIC_DEPENDENCY'
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
          onClick={() => navigate(`/insights?class=${encodeURIComponent(firstAffectedClass)}`)}
        >
          View Insights
        </button>
      </div>
    </aside>
  );
}
