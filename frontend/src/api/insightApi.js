const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function getInsights(projectId) {
  const response = await fetch(`${API_BASE_URL}/api/insights/${encodeURIComponent(projectId)}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch insights (${response.status})`);
  }

  return response.json();
}

export default {
  getInsights,
};
