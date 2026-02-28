import { Link, useLocation } from 'react-router-dom';
import { useProjectContext } from '../store/ProjectContext';

const links = [
  { to: '/', label: 'Dashboard' },
  { to: '/structure', label: '🌳 Structure Explorer', requiresProject: true },
  { to: '/metrics', label: 'Metrics' },
  { to: '/violations', label: 'Violations' },
  { to: '/graph', label: 'Dependency Graph' },
  { to: '/insights', label: 'Insights' },
  { to: '/compare', label: 'Compare' },
];

export default function MainLayout({ children, title = 'DevScope' }) {
  const location = useLocation();
  const { analysisId } = useProjectContext();

  return (
    <div className="ds-shell">
      <aside className="ds-sidebar">
        <h1>DevScope</h1>
        <nav>
          {links.filter((item) => !item.requiresProject || analysisId).map((item) => (
            <Link
              key={item.to}
              className={`ds-nav-link ${location.pathname === item.to ? 'active' : ''}`}
              to={item.to}
            >
              {item.label}
            </Link>
          ))}
        </nav>
      </aside>

      <main className="ds-main">
        <header className="ds-header">
          <h2>{title}</h2>
        </header>
        <div className="ds-content">{children}</div>
      </main>
    </div>
  );
}
