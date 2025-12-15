import { useHttpClient } from "@/composables/useHttpClient";

export function authApi() {
  const { get } = useHttpClient();

  return {
    me: () => get("/api/auth/me"),
  };
}
