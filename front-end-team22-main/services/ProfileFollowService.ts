// services/ProfileFollowService.ts

export type ProfileFollow = {
  id: number;
  locationId: number;
  radiusKm: number;
  lastChecked?: string;
};

/**
 * Haalt alle gevolgde banken op voor een profiel
 */
export async function getFollows(profileId: number): Promise<ProfileFollow[]> {
  const res = await fetch(`/api/profiles/${profileId}/follows`);

  if (!res.ok) {
    throw new Error(`Failed to load follows for profile ${profileId}`);
  }

  return res.json();
}

/**
 * Volg een bank met radius
 */
export async function followLocation(
  profileId: number,
  locationId: number,
  radiusKm: number
): Promise<void> {
  const res = await fetch(`/api/profiles/${profileId}/follows`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ locationId, radiusKm }),
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || "Failed to follow location");
  }
}

/**
 * Check of er nieuwe banken zijn binnen een follow
 */
export async function checkFollow(
  profileId: number,
  followId: number
): Promise<void> {
  const res = await fetch(
    `/api/profiles/${profileId}/follows/${followId}/check`
  );

  if (!res.ok) {
    throw new Error("Failed to check follow");
  }
}
