const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function getMetrics(analysisId) {
  const response = await fetch(`${API_BASE_URL}/api/metrics/${encodeURIComponent(analysisId)}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch metrics (${response.status})`);
  }
  return response.json();
}

export default {
  getMetrics,
};
