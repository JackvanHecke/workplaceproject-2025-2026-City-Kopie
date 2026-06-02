// pages/api/nearby-new.ts
import type { NextApiRequest, NextApiResponse } from "next";

const BACKEND = process.env.BACKEND_URL ?? "http://localhost:8080";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  const { lat, lon, radiusKm, since } = req.query;
  if (!lat || !lon)
    return res.status(400).json({ error: "lat and lon required" });

  const url = new URL(`/api/locations/nearby-new`);
  url.searchParams.set("lat", String(lat));
  url.searchParams.set("lon", String(lon));
  if (radiusKm) url.searchParams.set("radiusKm", String(radiusKm));
  if (since) url.searchParams.set("since", String(since));

  try {
    const r = await fetch(url.toString(), {
      headers: { accept: "application/json" },
    });
    const json = await r.json();
    return res.status(r.status).json(json);
  } catch (e) {
    console.error("nearby-new proxy error", e);
    res.status(500).json({ error: "proxy error" });
  }
}
