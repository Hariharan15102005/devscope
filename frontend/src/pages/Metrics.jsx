import { useEffect, useMemo, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import EmptyState from '../components/common/EmptyState';
import ErrorBox from '../components/common/ErrorBox';
import Loader from '../components/common/Loader';
import MainLayout from '../layouts/MainLayout';
import metricsApi from '../api/metricsApi';
import { useProjectContext } from '../store/ProjectContext';

export default function Metrics() {
  const { analysisId } = useProjectContext();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const focusedClass = searchParams.get('class');
  const [metricItems, setMetricItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    let active = true;
    async function loadMetrics() {
      if (!analysisId) {
        setMetricItems([]);
        return;
      }
      try {
        setLoading(true);
        setError('');
        const response = await metricsApi.getMetrics(analysisId);
        if (active) {
          setMetricItems(Array.isArray(response) ? response : []);
        }
      } catch (err) {
        if (active) {
          setError(err.message || 'Unable to load metrics.');
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    loadMetrics();
    return () => {
      active = false;
    };
  }, [analysisId]);

  const breadcrumb = useMemo(() => {
    if (!focusedClass) {
      return 'Metrics';
    }

    return `Metrics > ${focusedClass.split('.').at(-1)}`;
  }, [focusedClass, metricItems]);

  const visibleItems = useMemo(() => {
    if (!focusedClass) {
      return metricItems;
    }

    const normalized = focusedClass.toLowerCase();
    return metricItems.filter(
      (item) =>
        item.className.toLowerCase() === normalized ||
        item.className.toLowerCase().includes(normalized)
    );
  }, [focusedClass]);

  if (loading) {
    return (
      <MainLayout title="Metrics">
        <Loader label="Loading metrics..." />
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout title="Metrics">
        <ErrorBox message={error} />
      </MainLayout>
    );
  }

  return (
    <MainLayout title="Metrics">
      <section className="ds-breadcrumb">{breadcrumb}</section>
      <section className="ds-card">
        <h3>Class Metrics</h3>
        {!metricItems.length ? (
          <EmptyState title="No metrics found for this analysis." />
        ) : null}
        <ul className="ds-click-list">
          {(visibleItems.length ? visibleItems : metricItems).map((item) => (
            <li key={item.className}>
              <button type="button" onClick={() => navigate(`/graph?focus=${encodeURIComponent(item.className)}`)}>
                <span>
                  {item.className} · Dependencies {item.dependencyCount} · Methods {item.methodCount} · LOC {item.linesOfCode} · Risk {item.riskLevel}
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
