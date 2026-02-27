export default function ClassDetailPanel({ classDetail }) {
  if (!classDetail) {
    return (
      <section className="ds-card ds-structure-detail-empty">
        Select a class to view details
      </section>
    );
  }

  return (
    <section className="ds-card ds-structure-detail-card">
      <h3>{classDetail.className}</h3>
      <p className="ds-structure-detail-fullname">{classDetail.fullName}</p>
      <dl className="ds-structure-detail-grid">
        <div>
          <dt>Annotation</dt>
          <dd>@{classDetail.annotation}</dd>
        </div>
        <div>
          <dt>Risk Score</dt>
          <dd>{classDetail.riskScore}</dd>
        </div>
        <div>
          <dt>Methods</dt>
          <dd>{classDetail.methodCount}</dd>
        </div>
        <div>
          <dt>Dependencies</dt>
          <dd>{classDetail.dependencyCount}</dd>
        </div>
      </dl>
      <p>{classDetail.description}</p>
    </section>
  );
}
