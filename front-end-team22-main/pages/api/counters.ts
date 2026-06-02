// pages/api/counters.ts
import type { NextApiRequest, NextApiResponse } from "next";

const BACKEND = process.env.BACKEND_URL ?? "http://localhost:8080";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  try {
    const r = await fetch(`/api/locations/counters`, {
      headers: { accept: "application/json" },
    });
    const json = await r.json();
    return res.status(r.status).json(json);
  } catch (e) {
    console.error("counters proxy error", e);
    res.status(500).json({ error: "proxy error" });
  }
}
