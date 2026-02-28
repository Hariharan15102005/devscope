const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function uploadProjectZip(file) {
  const form = new FormData();
  form.append('file', file);

  const response = await fetch(`${API_BASE_URL}/api/projects/upload`, {
    method: 'POST',
    body: form,
  });

  if (!response.ok) {
    // Try to parse a JSON error response from the backend to get a helpful message
    let serverMessage = '';
    try {
      const json = await response.json();
      serverMessage = json?.message || json?.error || '';
    } catch (e) {
      // ignore parse errors
    }

    const message = serverMessage
      ? `Failed to upload project ZIP (${response.status}): ${serverMessage}`
      : `Failed to upload project ZIP (${response.status})`;
    throw new Error(message);
  }

  return response.json();
}

export default {
  uploadProjectZip,
};
