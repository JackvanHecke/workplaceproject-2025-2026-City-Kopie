import { useEffect } from "react";

export function useFollowNotifications(profileId: number, intervalMs = 30_000) {
  useEffect(() => {
    let cancelled = false;
    let timer: ReturnType<typeof setTimeout> | undefined;

    async function tick() {
      try {
        const resFollows = await fetch(`/api/profiles/${profileId}/follows`);
        if (!resFollows.ok) return;

        const follows: any[] = await resFollows.json();

        await Promise.all(
          follows.map((follow) =>
            fetch(`/api/profiles/${profileId}/follows/${follow.id}/check`)
          )
        );
      } catch (e) {
        if (!cancelled) console.error("Fout bij follow-check:", e);
      }

      if (!cancelled) timer = setTimeout(tick, intervalMs);
    }

    tick();
    return () => {
      cancelled = true;
      if (timer) clearTimeout(timer);
    };
  }, [profileId, intervalMs]);
}
