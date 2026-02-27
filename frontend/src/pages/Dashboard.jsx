import { useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import BarChart from '../components/charts/BarChart';
import ChartContainer from '../components/charts/ChartContainer';
import PieChart from '../components/charts/PieChart';
import EmptyState from '../components/common/EmptyState';
import ErrorBox from '../components/common/ErrorBox';
import Loader from '../components/common/Loader';
import StatCard from '../components/common/StatCard';
import MainLayout from '../layouts/MainLayout';
import useDashboard from '../hooks/useDashboard';
import { useProjectContext } from '../store/ProjectContext';

function getCountTone(value) {
  if (value <= 0) return 'red';
  if (value < 10) return 'yellow';
  return 'green';
}

function getDependencyTone(value) {
  if (value > 180) return 'red';
  if (value > 100) return 'yellow';
  return 'green';
}

function getHealthTone(value) {
  if (value >= 80) return 'green';
  if (value >= 60) return 'yellow';
  return 'red';
}

export default function Dashboard() {
  const { projectId } = useProjectContext();
  const { data, loading, error, refetch } = useDashboard(projectId);
  const navigate = useNavigate();

  const navigateToStructure = (search = '') => {
    navigate(`/structure${search}`, { state: { fromDashboard: true } });
  };

  const navigateToGraphFocus = (className) => {
    navigate(`/graph?focus=${encodeURIComponent(className)}`);
  };

  const layerData = useMemo(
    () =>
      Object.entries(data?.layerDistribution ?? {}).map(([label, value]) => ({
        label,
        value,
      })),
    [data]
  );

  const complexityData = useMemo(
    () =>
      Object.entries(data?.complexityDistribution ?? {}).map(([label, value]) => ({
        label,
        value,
      })),
    [data]
  );

  if (loading) {
    return (
      <MainLayout title="Dashboard">
        <Loader label="Loading dashboard..." />
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout title="Dashboard">
        <ErrorBox message={error} onRetry={refetch} />
      </MainLayout>
    );
  }

  if (!data) {
    return (
      <MainLayout title="Dashboard">
        <EmptyState title="No dashboard data found." />
      </MainLayout>
    );
  }

  const riskyClasses = data.topRiskyClasses?.slice(0, 5) ?? [];
  const topViolations = data.topViolations?.slice(0, 5) ?? [];

  return (
    <MainLayout title="Dashboard">
      <section className="ds-breadcrumb">Dashboard</section>

      <section className="ds-grid ds-stats-grid">
        <StatCard
          label="Total Packages"
          value={data.totalPackages}
          icon="📦"
          tone={getCountTone(data.totalPackages)}
          tooltip="Total number of packages"
        />
        <StatCard
          label="Total Classes"
          value={data.totalClasses}
          icon="📄"
          tone={getCountTone(data.totalClasses)}
          tooltip="Total number of classes"
          onClick={() => navigateToStructure()}
        />
        <StatCard
          label="Total Files"
          value={data.totalFiles}
          icon="🗂"
          tone={getCountTone(data.totalFiles)}
          tooltip="Total number of files"
        />
        <StatCard
          label="Total Dependencies"
          value={data.totalDependencies}
          icon="🔗"
          tone={getDependencyTone(data.totalDependencies)}
          tooltip="Total dependency edges"
        />
        <StatCard
          label="Health Score"
          value={data.healthScore}
          icon="❤️"
          tone={getHealthTone(data.healthScore)}
          tooltip="Overall project health score"
        />
      </section>

      <section className="ds-grid ds-chart-grid">
        <ChartContainer title="Layer Distribution">
          {layerData.length ? (
            <PieChart
              data={layerData}
              onItemClick={(layer) => navigate(`/graph?layer=${encodeURIComponent(layer)}`)}
            />
          ) : (
            <EmptyState title="No layer data." />
          )}
        </ChartContainer>
        <ChartContainer title="Complexity Distribution">
          {complexityData.length ? <BarChart data={complexityData} /> : <EmptyState title="No complexity data." />}
        </ChartContainer>
      </section>

      <section className="ds-card ds-risk-panel">
        <h3>Risk Overview</h3>
        <div className="ds-risk-grid">
          <article>
            <h4>🔴 Top Risky Classes</h4>
            {riskyClasses.length ? (
              <ul className="ds-click-list">
                {riskyClasses.map((item) => (
                  <li key={item.className}>
                    <button
                      type="button"
                      onClick={() => navigateToGraphFocus(item.className)}
                    >
                      <span>{item.className}</span>
                      <strong>{item.riskScore}</strong>
                    </button>
                  </li>
                ))}
              </ul>
            ) : (
              <EmptyState title="No risky classes." />
            )}
          </article>

          <article>
            <h4>⚠ Top Violations</h4>
            {topViolations.length ? (
              <ul className="ds-click-list">
                {topViolations.map((item) => (
                  <li key={item.violationId}>
                    <button
                      type="button"
                      onClick={() => navigate(`/violations?focus=${encodeURIComponent(item.violationId)}`)}
                    >
                      <span>{item.message}</span>
                      <strong>{item.severity}</strong>
                    </button>
                  </li>
                ))}
              </ul>
            ) : (
              <EmptyState title="No violations." />
            )}
          </article>

          <article>
            <h4>🔁 Cyclic Dependencies Count</h4>
            <button
              type="button"
              className="ds-cycle-button"
              onClick={() => navigate('/graph?highlight=cycles')}
            >
              {data.cyclicDependenciesCount}
            </button>
          </article>
        </div>
      </section>
    </MainLayout>
  );
}
