import { useNavigate } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';

const insights = [
  {
    id: 'I-201',
    type: 'class',
    title: 'Refactor DependencyResolver to reduce fan-out',
    className: 'DependencyResolver',
  },
  {
    id: 'I-202',
    type: 'cycle',
    title: 'Break cycle across rules-engine and metrics package',
  },
  {
    id: 'I-203',
    type: 'class',
    title: 'Simplify RuleOrchestrator branching logic',
    className: 'RuleOrchestrator',
  },
];

export default function Insights() {
  const navigate = useNavigate();

  function openInGraph(item) {
    if (item.type === 'cycle') {
      navigate('/graph?highlight=cycles');
      return;
    }

    navigate(`/graph?focus=${encodeURIComponent(item.className)}`);
  }

  return (
    <MainLayout title="Insights">
      <section className="ds-breadcrumb">Insights</section>
      <section className="ds-card">
        <h3>Suggested Improvements</h3>
        <ul className="ds-click-list">
          {insights.map((item) => (
            <li key={item.id}>
              <button type="button" onClick={() => openInGraph(item)}>
                <span>{item.title}</span>
                <strong>View in Graph</strong>
              </button>
            </li>
          ))}
        </ul>
      </section>
    </MainLayout>
  );
}
