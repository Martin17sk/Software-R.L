import api from "./axios";

export async function login(username, password) {
    const res = await api.post("/login", {username, password});
    return res.data;
}

export async function logout() {
  await api.post("/logout");
}

export async function me() {
  const res = await api.get("/me");
  return res.data;
}