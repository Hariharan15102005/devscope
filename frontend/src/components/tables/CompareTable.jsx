import { useNavigate } from 'react-router-dom';

const riskScore = {
  LOW: 1,
  MEDIUM: 2,
  HIGH: 3,
};

function getRiskRowTone(before, after) {
  const delta = (riskScore[after] || 0) - (riskScore[before] || 0);
  if (delta < 0) return 'improved';
  if (delta > 0) return 'worsened';
  return 'stable';
}

export default function CompareTable({ rows }) {
  const navigate = useNavigate();

  return (
    <section className="ds-card ds-compare-table-wrap">
      <table className="ds-compare-table">
        <thead>
          <tr>
            <th>Class Name</th>
            <th>LOC Difference</th>
            <th>Dependency Difference</th>
            <th>Risk Before</th>
            <th>Risk After</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr key={row.className} className={`tone-${getRiskRowTone(row.riskBefore, row.riskAfter)}`}>
              <td>{row.className}</td>
              <td>{row.locDiff > 0 ? `+${row.locDiff}` : row.locDiff}</td>
              <td>{row.dependencyDiff > 0 ? `+${row.dependencyDiff}` : row.dependencyDiff}</td>
              <td>{row.riskBefore}</td>
              <td>{row.riskAfter}</td>
              <td>
                <div className="ds-compare-actions">
                  <button type="button" className="ds-btn" onClick={() => navigate(`/metrics?class=${encodeURIComponent(row.className)}`)}>
                    Metrics
                  </button>
                  <button type="button" className="ds-btn" onClick={() => navigate(`/structure?class=${encodeURIComponent(row.className)}`)}>
                    Structure
                  </button>
                  <button type="button" className="ds-btn" onClick={() => navigate(`/graph?focus=${encodeURIComponent(row.className)}`)}>
                    Graph
                  </button>
                  <button type="button" className="ds-btn" onClick={() => navigate(`/violations?class=${encodeURIComponent(row.className)}`)}>
                    Violations
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
