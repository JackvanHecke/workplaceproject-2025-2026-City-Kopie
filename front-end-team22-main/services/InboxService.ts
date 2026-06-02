const BACKEND = process.env.NEXT_PUBLIC_API_URL; // of je proxy route

export async function getUnreadCount(profileId: number): Promise<number> {
  const r = await fetch(`${BACKEND}/inbox/${profileId}/unread-count`);
  if (!r.ok) return 0;
  const json = await r.json();
  return json.count ?? 0;
}

export async function getInbox(profileId: number) {
  const r = await fetch(`${BACKEND}/inbox/${profileId}`);
  if (!r.ok) return [];
  return r.json();
}

export async function markRead(profileId: number, profileMessageId: number) {
  await fetch(`${BACKEND}/inbox/${profileId}/${profileMessageId}/read`, { method: "POST" });
}
