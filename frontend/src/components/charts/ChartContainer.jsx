export default function ChartContainer({ title, children }) {
  return (
    <section className="ds-card ds-chart-container">
      <h3>{title}</h3>
      {children}
    </section>
  );
}
