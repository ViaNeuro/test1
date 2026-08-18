const API = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
export const authHeader = (login) => `Basic ${btoa(`${login.username}:${login.password}`)}`;
export async function request(path, login, options = {}) {
  const res = await fetch(`${API}${path}`, { ...options, headers: { 'Content-Type': 'application/json', Authorization: authHeader(login), ...(options.headers || {}) } });
  if (!res.ok) throw new Error((await res.json().catch(() => ({}))).error || res.statusText);
  return res.json();
}
