export default function ClassDetailPanel({ className }) {
  if (!className) {
    return null;
  }

  return (
    <section className="ds-card">
      <h3>Class Details</h3>
      <p>{className}</p>
    </section>
  );
}
