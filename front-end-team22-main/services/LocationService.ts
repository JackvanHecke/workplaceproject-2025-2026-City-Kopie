// services/LocationService.ts
import { Location } from "../types";

const apiUrl = "/api";

const BACKEND_BASE_URL = (process.env.NEXT_PUBLIC_API_URL || "").replace(
  /\/$/,
  ""
);

const toStringArray = (value: any): string[] => {
  if (Array.isArray(value)) {
    return value.map((v) => String(v).trim()).filter(Boolean);
  }
  if (typeof value === "string") {
    return value
      .split(",")
      .map((t) => t.trim())
      .filter((t) => t.length > 0);
  }
  return [];
};

// Normalize backend DTO -> frontend Location
const normalizeLocation = (b: any): Location => {
  // keep all your existing mappings above ...

  const rawPhotoUrl = b.photoUrl ?? b.photo_url ?? "";

  const resolvedPhotoUrl =
    rawPhotoUrl &&
    !/^https?:\/\//i.test(rawPhotoUrl) && // only prefix if not already absolute
    BACKEND_BASE_URL
      ? `${BACKEND_BASE_URL}${
          rawPhotoUrl.startsWith("/") ? "" : "/"
        }${rawPhotoUrl}`
      : rawPhotoUrl;

  return {
    id: b.id ?? b.benchId,

    benchName: b.benchName ?? b.benchName,
    owner: b.owner ?? b.benchOwner ?? "",
    street: b.street ?? b.benchStreet ?? "",
    houseNumber: b.houseNumber ?? b.housenumber ?? b.benchHouseNumber ?? "",
    zipCode: b.zipCode ?? b.postalCode ?? b.zip ?? b.benchPostalCode ?? "",
    city: b.city ?? b.benchCity ?? "",
    country: b.country ?? b.benchCountry ?? "",
    size: b.size ?? b.benchSize ?? "",
    type: b.type ?? b.benchType ?? "",
    connectedRoutes: b.connectedRoutes ?? 0,
    tags: toStringArray(b.tags),

    showInApp:
      typeof b.showInApp === "boolean"
        ? b.showInApp
        : typeof b.visibleInApp === "boolean"
        ? b.visibleInApp
        : false,

    mobile: typeof b.mobile === "boolean" ? b.mobile : undefined,

    publicAvailable:
      typeof b.publicAvailable === "boolean" ? b.publicAvailable : undefined,

    latitude:
      typeof b.latitude === "number"
        ? b.latitude
        : typeof b.lat === "number"
        ? b.lat
        : undefined,

    longitude:
      typeof b.longitude === "number"
        ? b.longitude
        : typeof b.lng === "number"
        ? b.lng
        : undefined,

    stations: toStringArray(b.stations),

    photoUrl: resolvedPhotoUrl,
    movementHistory: b.movementHistory ?? b.movement_history ?? [],
    completedChecklistItems: b.completedChecklistItems ?? [],

    movementRouteId: b.movementRouteId ?? null,
    movementRouteName: b.movementRouteName ?? null,
    movementRouteType: b.movementRouteType ?? null,
  };
};

const getAll = (): Promise<Location[]> => {
  return fetch(`${apiUrl}/locations`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Failed to load locations: ${res.status}`);
      }
      return res.json();
    })
    .then((list) =>
      Array.isArray(list) ? list.map((b) => normalizeLocation(b)) : []
    );
};

const getAllNames = (): Promise<string[]> => {
  return fetch(`${apiUrl}/locations/names`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  }).then((res) => {
    if (!res.ok) {
      throw new Error(`Failed to load location names: ${res.status}`);
    }
    return res.json();
  });
};

const getMine = (owner: string): Promise<Location[]> => {
  const url = `${apiUrl}/locations/mine?owner=${encodeURIComponent(owner)}`;
  return fetch(url, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then((res) => {
      if (!res.ok)
        throw new Error(`Failed to load owned locations: ${res.status}`);
      return res.json();
    })
    .then((list) =>
      Array.isArray(list) ? list.map((b) => normalizeLocation(b)) : []
    );
};

const getById = (id: number | string): Promise<Location> => {
  return fetch(`${apiUrl}/locations/${id}`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Failed to load location ${id}: ${res.status}`);
      }
      return res.json();
    })
    .then((b) => normalizeLocation(b));
};

const create = (payload: Partial<Location>): Promise<Location> => {
  return fetch(`${apiUrl}/locations`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Failed to create location: ${res.status}`);
      }
      return res.json();
    })
    .then((b) => normalizeLocation(b));
};

const update = (
  id: number | string,
  payload: Partial<Location>
): Promise<Location> => {
  return fetch(`${apiUrl}/locations/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`Failed to update location ${id}: ${res.status}`);
      }
      return res.json();
    })
    .then((b) => normalizeLocation(b));
};

const remove = (id: number | string) => {
  return fetch(`${apiUrl}/locations/${id}`, {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
  }).then((res) => res.ok);
};

// file upload for bench photo
const uploadPhoto = (id: number | string, file: File): Promise<Location> => {
  const formData = new FormData();
  formData.append("file", file);

  return fetch(`${apiUrl}/locations/${id}/photo`, {
    method: "POST",
    body: formData,
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(
          `Failed to upload photo for bench ${id}: ${res.status}`
        );
      }
      return res.json();
    })
    .then((b) => normalizeLocation(b));
};

const LocationService = {
  getAll,
  getMine,
  getById,
  create,
  update,
  remove,
  uploadPhoto,
  getAllNames,
};

export { normalizeLocation };
export default LocationService;
