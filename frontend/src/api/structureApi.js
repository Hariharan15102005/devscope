const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function getTree(analysisId, filterType) {
  const search = new URLSearchParams();
  if (filterType) {
    search.set('filter', filterType);
  }

  const response = await fetch(
    `${API_BASE_URL}/api/structure/${encodeURIComponent(analysisId)}${search.toString() ? `?${search.toString()}` : ''}`
  );

  if (!response.ok) {
    throw new Error(`Failed to fetch structure tree (${response.status})`);
  }

  return response.json();
}

async function getClassDetail(analysisId, className) {
  const response = await fetch(
    `${API_BASE_URL}/api/structure/${encodeURIComponent(analysisId)}/class?name=${encodeURIComponent(className)}`
  );

  if (!response.ok) {
    throw new Error(`Failed to fetch class details (${response.status})`);
  }

  return response.json();
}

export default {
  getTree,
  getClassDetail,
};
