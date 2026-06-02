const apiUrl = "/api";

export function getToken(): string | null {
  if (typeof document === "undefined") return null; // SSR safety check

  const name = "token=";
  const decodedCookie = decodeURIComponent(document.cookie);
  const cookies = decodedCookie.split(";");

  for (let cookie of cookies) {
    cookie = cookie.trim();
    if (cookie.startsWith(name)) {
      return cookie.substring(name.length);
    }
  }

  return null;
}

const getAll = () => {
  return fetch("/api/profile", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
};

const getAllCurrentUserData = (email) => {
  return fetch(`/api/profile/${email}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
};

const getCurrentUser = async () => {
  const res = await fetch(`/api/profile/token`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + getToken(),
    },
  });

  if (res.status === 401) {
    return null;
  }

  if (!res.ok) {
    throw new Error("Failed to fetch current user");
  }

  const text = await res.text();
  if (!text) return null;
  return JSON.parse(text);
};

const UserService = {
  getAll,
  getCurrentUser,
  getAllCurrentUserData,
};

export default UserService;
