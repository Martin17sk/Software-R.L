import { ref } from "vue";

const baseURL = import.meta.env.VITE_API_BASE_URL;
const authToken = ref(localStorage.getItem("authToken"));

export function useHttpClient() {
  function setAuthToken(token) {
    authToken.value = token;
    localStorage.setItem("authToken", token);
  }

  function clearAuthToken() {
    authToken.value = null;
    localStorage.removeItem("authToken");
  }

  async function request(path, options = {}) {
    const headers = {
      ...(options.headers || {}),
    };

    const isFormData = options.body instanceof FormData;
    if (!isFormData && !headers["Content-Type"]) {
      headers["Content-Type"] = "application/json";
    }

    const res = await fetch(`${baseURL}${path}`, {
      ...options,
      headers,
      credentials: "include",
    });

    const contentType = res.headers.get("content-type") || "";
    const body = contentType.includes("application/json")
      ? await res.json().catch(() => null)
      : await res.text().catch(() => "");

    if (!res.ok) {
      const msg =
        typeof body === "string" && body
          ? body
          : body?.message || `${res.status} ${res.statusText}`;
      throw new Error(msg);
    }

    return body;
  }

  const get = (path, options = {}) => request(path, { ...options, method: "GET" });

  const post = (path, data, options = {}) => {
    const isFormData = data instanceof FormData;
    return request(path, {
      ...options,
      method: "POST",
      body: isFormData ? data : JSON.stringify(data)
    })
  }

  const put = (path, data, options = {}) => {
    const isFormData = data instanceof FormData;
    return request(path, {
      ...options,
      method: "PUT",
      body: isFormData ? data : JSON.stringify(data),
    });
  };

  const del = (path, options = {}) => request(path, { ...options, method: "DELETE" });

  return { request, get, post, put, del, setAuthToken, clearAuthToken };
}
