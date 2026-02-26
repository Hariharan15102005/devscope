import { BrowserRouter } from 'react-router-dom';
import AppRoutes from './routes/AppRoutes';
import { ProjectProvider } from './store/ProjectContext';

export default function App() {
  return (
    <ProjectProvider>
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </ProjectProvider>
  );
}
