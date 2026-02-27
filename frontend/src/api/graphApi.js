const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function getGraph(projectId) {
  const response = await fetch(`${API_BASE_URL}/api/graph/${encodeURIComponent(projectId)}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch dependency graph (${response.status})`);
  }

  return response.json();
}

export default {
  getGraph,
};
