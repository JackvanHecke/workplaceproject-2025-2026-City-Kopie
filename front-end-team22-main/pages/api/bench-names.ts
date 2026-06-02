import type { NextApiRequest, NextApiResponse } from "next";

export default async function handler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  try {
    const r = await fetch(`/api/locations/names`, {
      headers: { accept: "application/json" },
    });

    const text = await r.text();

    try {
      const json = JSON.parse(text);
      return res.status(r.status).json(json);
    } catch {
      return res
        .status(502)
        .json({ error: "Backend did not return JSON", raw: text });
    }
  } catch (e: any) {
    console.error("bench-names proxy error", e);
    return res.status(500).json({ error: "proxy error", message: String(e) });
  }
}
