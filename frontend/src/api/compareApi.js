const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function getVersions(analysisId) {
  const response = await fetch(`${API_BASE_URL}/api/compare/versions/${encodeURIComponent(analysisId)}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch analysis versions (${response.status})`);
  }
  return response.json();
}

async function compare(baseAnalysisId, targetAnalysisId) {
  const response = await fetch(`${API_BASE_URL}/api/compare`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ baseAnalysisId, targetAnalysisId }),
  });

  if (!response.ok) {
    throw new Error(`Failed to compare analyses (${response.status})`);
  }

  return response.json();
}

export default {
  getVersions,
  compare,
};
