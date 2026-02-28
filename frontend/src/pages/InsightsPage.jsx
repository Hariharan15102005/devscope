import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import EmptyState from '../components/common/EmptyState';
import ErrorBox from '../components/common/ErrorBox';
import FilterDropdown from '../components/common/FilterDropdown';
import Loader from '../components/common/Loader';
import SearchBox from '../components/common/SearchBox';
import InsightCard from '../components/panels/InsightCard';
import InsightDetailPanel from '../components/panels/InsightDetailPanel';
import useInsights from '../hooks/useInsights';
import MainLayout from '../layouts/MainLayout';
import { useProjectContext } from '../store/ProjectContext';

const impactOptions = [
  { label: 'All Impact Levels', value: 'All' },
  { label: 'High', value: 'HIGH' },
  { label: 'Medium', value: 'MEDIUM' },
  { label: 'Low', value: 'LOW' },
];

const categoryOptions = [
  { label: 'All Categories', value: 'All' },
  { label: 'Complexity', value: 'COMPLEXITY' },
  { label: 'Coupling', value: 'COUPLING' },
  { label: 'Layering', value: 'LAYERING' },
  { label: 'Cycle', value: 'CYCLE' },
];

export default function InsightsPage() {
  const { analysisId } = useProjectContext();
  const location = useLocation();

  const {
    loading,
    error,
    refetch,
    filters,
    setFilters,
    clearFilters,
    filteredInsights,
    selectedInsight,
    selectedInsightId,
    setSelectedInsightId,
    summary,
    applyQueryParams,
  } = useInsights(analysisId);

  useEffect(() => {
    applyQueryParams(location.search);
  }, [location.search, applyQueryParams]);

  if (loading) {
    return (
      <MainLayout title="Insights">
        <Loader label="Loading recommendations..." />
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout title="Insights">
        <ErrorBox message={error} onRetry={refetch} />
      </MainLayout>
    );
  }

  return (
    <MainLayout title="Insights">
      <section className="ds-breadcrumb">Dashboard &gt; Insights</section>

      <section className="ds-grid ds-insights-summary-grid">
        <article className="ds-card ds-insight-summary-item">
          <h3>Total Recommendations</h3>
          <p>{summary.total}</p>
        </article>
        <article className="ds-card ds-insight-summary-item ds-impact-high">
          <h3>High Impact</h3>
          <p>{summary.high}</p>
        </article>
        <article className="ds-card ds-insight-summary-item ds-impact-medium">
          <h3>Medium Impact</h3>
          <p>{summary.medium}</p>
        </article>
        <article className="ds-card ds-insight-summary-item ds-impact-low">
          <h3>Low Impact</h3>
          <p>{summary.low}</p>
        </article>
      </section>

      <section className="ds-card ds-insights-filters">
        <FilterDropdown
          label="Impact"
          value={filters.impact}
          options={impactOptions}
          onChange={(value) => setFilters((prev) => ({ ...prev, impact: value }))}
        />
        <FilterDropdown
          label="Category"
          value={filters.category}
          options={categoryOptions}
          onChange={(value) => setFilters((prev) => ({ ...prev, category: value }))}
        />
        <SearchBox
          value={filters.search}
          onChange={(value) => setFilters((prev) => ({ ...prev, search: value }))}
          placeholder="Search by class name or recommendation title"
        />
        <button type="button" className="ds-btn" onClick={clearFilters}>
          Clear Filters
        </button>
      </section>

      {!filteredInsights.length ? (
        <EmptyState title="No recommendations found for the selected filters." />
      ) : (
        <section className="ds-insights-layout">
          <div className="ds-insights-list">
            {filteredInsights.map((insight) => (
              <InsightCard
                key={insight.id}
                insight={insight}
                selected={selectedInsightId === insight.id}
                onSelect={setSelectedInsightId}
              />
            ))}
          </div>

          <div className="ds-insights-detail-wrap">
            <InsightDetailPanel insight={selectedInsight} isOpen={Boolean(selectedInsight)} />
          </div>
        </section>
      )}
    </MainLayout>
  );
}
