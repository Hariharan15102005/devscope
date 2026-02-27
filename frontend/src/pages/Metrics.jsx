import { useMemo } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';

const metricItems = [
  { className: 'DependencyResolver', coupling: 17, complexity: 14 },
  { className: 'RuleOrchestrator', coupling: 14, complexity: 16 },
  { className: 'RepositoryScanner', coupling: 9, complexity: 10 },
  { className: 'DashboardService', coupling: 6, complexity: 7 },
];

export default function Metrics() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const focusedClass = searchParams.get('class');

  const breadcrumb = useMemo(() => {
    if (!focusedClass) {
      return 'Metrics';
    }

    return `Metrics > ${focusedClass.split('.').at(-1)}`;
  }, [focusedClass]);

  const visibleItems = useMemo(() => {
    if (!focusedClass) {
      return metricItems;
    }

    const normalized = focusedClass.toLowerCase();
    return metricItems.filter(
      (item) => item.className.toLowerCase() === normalized || item.className.toLowerCase().includes(normalized)
    );
  }, [focusedClass]);

  return (
    <MainLayout title="Metrics">
      <section className="ds-breadcrumb">{breadcrumb}</section>
      <section className="ds-card">
        <h3>Class Metrics</h3>
        <ul className="ds-click-list">
          {(visibleItems.length ? visibleItems : metricItems).map((item) => (
            <li key={item.className}>
              <button type="button" onClick={() => navigate(`/graph?focus=${encodeURIComponent(item.className)}`)}>
                <span>
                  {item.className} · Coupling {item.coupling} · Complexity {item.complexity}
                </span>
                <strong>View Dependencies</strong>
              </button>
            </li>
          ))}
        </ul>
      </section>
    </MainLayout>
  );
}
