import { useEffect, useMemo, useRef, useState } from 'react';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import ClassDetailPanel from '../components/panels/ClassDetailPanel';
import EmptyState from '../components/common/EmptyState';
import ErrorBox from '../components/common/ErrorBox';
import Loader from '../components/common/Loader';
import MainLayout from '../layouts/MainLayout';
import structureApi from '../api/structureApi';
import { useProjectContext } from '../store/ProjectContext';

export default function StructureExplorer() {
  const { analysisId } = useProjectContext();
  const location = useLocation();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  const queryParams = new URLSearchParams(location.search);
  const selectedClass = queryParams.get('class');
  const filterType = queryParams.get('filter');

  const [treeData, setTreeData] = useState([]);
  const [selectedClassName, setSelectedClassName] = useState(selectedClass || null);
  const [selectedClassDetail, setSelectedClassDetail] = useState(null);
  const [expandedPackages, setExpandedPackages] = useState({});
  const [loadingTree, setLoadingTree] = useState(true);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [error, setError] = useState('');
  const [retryToken, setRetryToken] = useState(0);

  const nodeRefs = useRef({});

  const fromDashboard = Boolean(location.state?.fromDashboard);

  useEffect(() => {
    let active = true;

    async function loadTree() {
      setLoadingTree(true);
      setError('');
      try {
        const response = await structureApi.getTree(analysisId, filterType || undefined);
        if (!active) {
          return;
        }

        const packages = response.packages || [];
        setTreeData(packages);

        const initialExpanded = {};
        packages.forEach((item) => {
          initialExpanded[item.packageName] = true;
        });
        setExpandedPackages(initialExpanded);
      } catch (err) {
        if (active) {
          setError(err.message || 'Unable to load structure tree.');
        }
      } finally {
        if (active) {
          setLoadingTree(false);
        }
      }
    }

    loadTree();

    return () => {
      active = false;
    };
  }, [analysisId, filterType, retryToken]);

  useEffect(() => {
    setSelectedClassName(selectedClass || null);
  }, [selectedClass]);

  async function loadClassDetails(className) {
    if (!className) {
      setSelectedClassDetail(null);
      return;
    }

    setLoadingDetail(true);
    try {
      const detail = await structureApi.getClassDetail(analysisId, className);
      setSelectedClassDetail(detail);
    } catch {
      setSelectedClassDetail(null);
    } finally {
      setLoadingDetail(false);
    }
  }

  useEffect(() => {
    if (selectedClassName) {
      loadClassDetails(selectedClassName);
      return;
    }

    setSelectedClassDetail(null);
  }, [analysisId, selectedClassName]);

  useEffect(() => {
    if (!selectedClassName || !treeData.length) {
      return;
    }

    const selectedPackage = treeData.find((pkg) =>
      (pkg.classes || []).some((item) => item.fullName === selectedClassName || item.className === selectedClassName)
    );

    if (selectedPackage) {
      setExpandedPackages((prev) => ({
        ...prev,
        [selectedPackage.packageName]: true,
      }));
    }

    const node = nodeRefs.current[selectedClassName];
    if (node) {
      node.scrollIntoView({ block: 'center', behavior: 'smooth' });
    }
  }, [selectedClassName, treeData]);

  const breadcrumb = useMemo(() => {
    const root = fromDashboard ? 'Dashboard > Structure Explorer' : 'Structure Explorer';
    const selected = selectedClassDetail?.className || selectedClassName?.split('.').at(-1);
    return selected ? `${root} > ${selected}` : root;
  }, [fromDashboard, selectedClassDetail?.className, selectedClassName]);

  function setClassSelection(className) {
    const nextParams = new URLSearchParams(location.search);
    nextParams.set('class', className);
    setSearchParams(nextParams, { replace: true });
    setSelectedClassName(className);
  }

  function togglePackage(packageName) {
    setExpandedPackages((prev) => ({
      ...prev,
      [packageName]: !prev[packageName],
    }));
  }

  if (loadingTree) {
    return (
      <MainLayout title="Structure Explorer">
        <Loader label="Loading structure explorer..." />
      </MainLayout>
    );
  }

  if (error) {
    return (
      <MainLayout title="Structure Explorer">
        <ErrorBox message={error} onRetry={() => setRetryToken((prev) => prev + 1)} />
      </MainLayout>
    );
  }

  return (
    <MainLayout title="Structure Explorer">
      <section className="ds-breadcrumb">{breadcrumb}</section>

      {filterType ? (
        <section className="ds-structure-filter-badge">Filter: @{filterType}</section>
      ) : null}

      <section className="ds-structure-layout">
        <article className="ds-card ds-structure-tree-card">
          <h3>Project Structure</h3>
          {!treeData.length ? (
            <EmptyState title="No classes found for this filter." />
          ) : (
            <ul className="ds-structure-packages">
              {treeData.map((pkg) => (
                <li key={pkg.packageName}>
                  <button
                    className="ds-structure-package-btn"
                    type="button"
                    onClick={() => togglePackage(pkg.packageName)}
                  >
                    <span>{expandedPackages[pkg.packageName] ? '▾' : '▸'}</span>
                    <strong>{pkg.packageName}</strong>
                  </button>

                  {expandedPackages[pkg.packageName] ? (
                    <ul className="ds-structure-classes">
                      {(pkg.classes || []).map((item) => {
                        const isSelected = selectedClassName === item.fullName || selectedClassName === item.className;
                        const isFiltered = Boolean(filterType) && item.annotation.toLowerCase() === filterType.toLowerCase();
                        const nodeKey = item.fullName;

                        return (
                          <li key={item.fullName}>
                            <button
                              ref={(element) => {
                                nodeRefs.current[nodeKey] = element;
                                if (item.className === selectedClassName) {
                                  nodeRefs.current[selectedClassName] = element;
                                }
                              }}
                              type="button"
                              className={`ds-structure-class-btn ${isSelected ? 'is-selected' : ''} ${
                                isFiltered ? 'is-filtered' : ''
                              }`}
                              onClick={() => setClassSelection(item.fullName)}
                            >
                              <span>{item.className}</span>
                              <small>@{item.annotation}</small>
                            </button>
                          </li>
                        );
                      })}
                    </ul>
                  ) : null}
                </li>
              ))}
            </ul>
          )}
        </article>

        <article>
          {loadingDetail ? (
            <Loader label="Loading class details..." />
          ) : (
            <ClassDetailPanel
              classDetail={selectedClassDetail}
              onViewInGraph={(className) => navigate(`/graph?focus=${encodeURIComponent(className)}`)}
            />
          )}
        </article>
      </section>
    </MainLayout>
  );
}
