import { useMemo } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';

const violationItems = [
  {
    id: 'V-1021',
    type: 'class',
    className: 'DependencyResolver',
    message: 'High cyclomatic complexity in DependencyResolver',
  },
  {
    id: 'V-1108',
    type: 'cycle',
    message: 'Cyclic dependency between RuleOrchestrator and ClassMetricsCalculator',
  },
  {
    id: 'V-1042',
    type: 'class',
    className: 'RuleOrchestrator',
    message: 'Field injection detected in RuleOrchestrator',
  },
];

export default function Violations() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const focus = searchParams.get('focus');
  const focusedClass = searchParams.get('class');

  const items = useMemo(() => {
    if (focus) {
      return violationItems.filter((item) => item.id === focus);
    }
    if (focusedClass) {
      const normalized = focusedClass.toLowerCase();
      return violationItems.filter((item) => (item.className || '').toLowerCase().includes(normalized));
    }
    return violationItems;
  }, [focus, focusedClass]);

  function viewInGraph(item) {
    if (item.type === 'cycle') {
      navigate('/graph?highlight=cycles');
      return;
    }

    navigate(`/graph?focus=${encodeURIComponent(item.className)}`);
  }

  return (
    <MainLayout title="Violations">
      <section className="ds-breadcrumb">Violations</section>
      <section className="ds-card">
        <h3>Detected Violations</h3>
        <ul className="ds-click-list">
          {(items.length ? items : violationItems).map((item) => (
            <li key={item.id}>
              <button type="button" onClick={() => viewInGraph(item)}>
                <span>{item.message}</span>
                <strong>View in Graph</strong>
              </button>
            </li>
          ))}
        </ul>
      </section>
    </MainLayout>
  );
}
