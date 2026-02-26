import { Link, useLocation } from 'react-router-dom';

const links = [
  { to: '/', label: 'Dashboard' },
  { to: '/metrics', label: 'Metrics' },
  { to: '/violations', label: 'Violations' },
  { to: '/graph', label: 'Dependency Graph' },
];

export default function MainLayout({ children, title = 'DevScope' }) {
  const location = useLocation();

  return (
    <div className="ds-shell">
      <aside className="ds-sidebar">
        <h1>DevScope</h1>
        <nav>
          {links.map((item) => (
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
