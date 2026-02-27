import { useNavigate } from 'react-router-dom';
import Loader from '../common/Loader';

export default function GraphSidePanel({ isOpen, detail, loading }) {
  const navigate = useNavigate();

  if (!isOpen) {
    return null;
  }

  return (
    <aside className="ds-card ds-graph-sidepanel">
      {loading ? <Loader label="Loading class details..." /> : null}

      {!loading && detail ? (
        <>
          <h3>{detail.name}</h3>
          <p className="ds-structure-detail-fullname">{detail.fullName}</p>

          <dl className="ds-structure-detail-grid">
            <div>
              <dt>Layer</dt>
              <dd>{detail.layer}</dd>
            </div>
            <div>
              <dt>Dependencies</dt>
              <dd>{detail.dependencyCount}</dd>
            </div>
            <div>
              <dt>Dependents</dt>
              <dd>{detail.dependentCount}</dd>
            </div>
          </dl>

          <div className="ds-graph-sidepanel-list-wrap">
            <h4>Outgoing Dependencies</h4>
            <ul className="ds-graph-sidepanel-list">
              {(detail.outgoingDependencies || []).length ? (
                detail.outgoingDependencies.map((item) => <li key={`out-${item}`}>{item}</li>)
              ) : (
                <li>None</li>
              )}
            </ul>
          </div>

          <div className="ds-graph-sidepanel-list-wrap">
            <h4>Incoming Dependents</h4>
            <ul className="ds-graph-sidepanel-list">
              {(detail.incomingDependents || []).length ? (
                detail.incomingDependents.map((item) => <li key={`in-${item}`}>{item}</li>)
              ) : (
                <li>None</li>
              )}
            </ul>
          </div>

          <div className="ds-graph-sidepanel-actions">
            <button type="button" className="ds-btn" onClick={() => navigate(`/structure?class=${encodeURIComponent(detail.fullName)}`)}>
              View in Structure
            </button>
            <button type="button" className="ds-btn" onClick={() => navigate(`/metrics?class=${encodeURIComponent(detail.fullName)}`)}>
              View Metrics
            </button>
            <button
              type="button"
              className="ds-btn"
              onClick={() => navigate(`/violations?class=${encodeURIComponent(detail.fullName)}`)}
            >
              View Violations
            </button>
          </div>
        </>
      ) : null}
    </aside>
  );
}
