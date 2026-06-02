// services/LocationPartnershipService.ts
import type { LocationPartnershipDTO } from "../types";

const apiUrl = "/api";

async function getForLocation(benchId: number): Promise<LocationPartnershipDTO[]> {
  const res = await fetch(`${apiUrl}/locations/${benchId}/partnerships`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });

  if (!res.ok) {
    throw new Error(`Failed to fetch partnerships for bench ${benchId}: ${res.status}`);
  }

  const data = await res.json();
  return Array.isArray(data) ? data : [];
}

async function toggle(
  benchId: number,
  categoryId: number
): Promise<LocationPartnershipDTO> {
  const res = await fetch(
    `${apiUrl}/locations/${benchId}/partnerships/${categoryId}/toggle`,
    {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
    }
  );

  if (!res.ok) {
    throw new Error(`Failed to toggle partnership for bench ${benchId}: ${res.status}`);
  }

  return res.json();
}

const LocationPartnershipService = { getForLocation, toggle };
export default LocationPartnershipService;
