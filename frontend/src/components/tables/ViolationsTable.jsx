import SeverityBadge from '../common/SeverityBadge';

export default function ViolationsTable({
  rows,
  selectedViolationId,
  onSelectViolation,
  onQuickAction,
  severitySortDirection,
  onToggleSeveritySort,
}) {
  return (
    <section className="ds-card ds-violations-table-wrap">
      <table className="ds-violations-table">
        <thead>
          <tr>
            <th>
              <button type="button" className="ds-sort-btn" onClick={onToggleSeveritySort}>
                Severity {severitySortDirection === 'desc' ? '↓' : '↑'}
              </button>
            </th>
            <th>Type</th>
            <th>Title</th>
            <th>Affected Classes</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr
              key={row.id}
              className={selectedViolationId === row.id ? 'is-selected' : ''}
              onClick={() => onSelectViolation(row.id)}
            >
              <td>
                <SeverityBadge severity={row.severity} />
              </td>
              <td>{row.type}</td>
              <td>{row.title}</td>
              <td>{(row.affectedClasses || []).join(', ')}</td>
              <td>
                <button
                  type="button"
                  className="ds-btn"
                  onClick={(event) => {
                    event.stopPropagation();
                    onQuickAction(row);
                  }}
                >
                  Open
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
