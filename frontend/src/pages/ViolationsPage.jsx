import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import EmptyState from '../components/common/EmptyState';
import ErrorBox from '../components/common/ErrorBox';
import FilterDropdown from '../components/common/FilterDropdown';
import Loader from '../components/common/Loader';
import SearchBox from '../components/common/SearchBox';
import ViolationDetailPanel from '../components/panels/ViolationDetailPanel';
import ViolationsTable from '../components/tables/ViolationsTable';
import useViolations from '../hooks/useViolations';
import MainLayout from '../layouts/MainLayout';
import { useProjectContext } from '../store/ProjectContext';

const severityOptions = [
  { label: 'All Severities', value: 'All' },
  { label: 'High', value: 'HIGH' },
  { label: 'Medium', value: 'MEDIUM' },
  { label: 'Low', value: 'LOW' },
];

const typeOptions = [
  { label: 'All Types', value: 'All' },
  { label: 'Layer Violation', value: 'LAYER_VIOLATION' },
  { label: 'Cyclic Dependency', value: 'CYCLIC_DEPENDENCY' },
  { label: 'God Class', value: 'GOD_CLASS' },
  { label: 'High Coupling', value: 'HIGH_COUPLING' },
];

export default function ViolationsPage() {
  const { analysisId } = useProjectContext();
  const location = useLocation();

  const {
    loading,
    error,
    refetch,
    filters,
    setFilters,
    clearFilters,
    filteredViolations,
    selectedViolation,
    selectedViolationId,
    setSelectedViolationId,
    severitySortDirection,
    toggleSeveritySortDirection,
    applyQueryParams,
    summary,
  } = useViolations(analysisId);

  useEffect(() => {
    applyQueryParams(location.search);
  }, [location.search, applyQueryParams]);

  if (loading) {
    return (
      <MainLayout title="Violations">
        <Loader label="Loading violations..." />
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout title="Violations">
        <ErrorBox message={error} onRetry={refetch} />
      </MainLayout>
    );
  }

  return (
    <MainLayout title="Violations">
      <section className="ds-breadcrumb">Dashboard &gt; Violations</section>

      <section className="ds-card ds-violations-summary">
        {summary.total} Violations ({summary.high} High, {summary.medium} Medium, {summary.low} Low)
      </section>

      <section className="ds-card ds-violations-filters">
        <FilterDropdown
          label="Severity"
          value={filters.severity}
          options={severityOptions}
          onChange={(value) => setFilters((prev) => ({ ...prev, severity: value }))}
        />
        <FilterDropdown
          label="Type"
          value={filters.type}
          options={typeOptions}
          onChange={(value) => setFilters((prev) => ({ ...prev, type: value }))}
        />
        <SearchBox
          value={filters.search}
          onChange={(value) => setFilters((prev) => ({ ...prev, search: value }))}
          placeholder="Search by class name or title"
        />
        <button type="button" className="ds-btn" onClick={clearFilters}>
          Clear Filters
        </button>
      </section>

      {!filteredViolations.length ? (
        <EmptyState title="No violations found for the selected filters." />
      ) : (
        <section className="ds-violations-layout">
          <div className="ds-violations-left">
            <ViolationsTable
              rows={filteredViolations}
              selectedViolationId={selectedViolationId}
              onSelectViolation={setSelectedViolationId}
              onQuickAction={(item) => setSelectedViolationId(item.id)}
              severitySortDirection={severitySortDirection}
              onToggleSeveritySort={toggleSeveritySortDirection}
            />
          </div>
          <div className="ds-violations-right">
            <ViolationDetailPanel violation={selectedViolation} isOpen={Boolean(selectedViolation)} />
          </div>
        </section>
      )}
    </MainLayout>
  );
}
