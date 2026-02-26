const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function getSummary(projectId) {
  const response = await fetch(`${API_BASE_URL}/api/dashboard/${encodeURIComponent(projectId)}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch dashboard summary (${response.status})`);
  }

  return response.json();
}

export default {
  getSummary,
};
