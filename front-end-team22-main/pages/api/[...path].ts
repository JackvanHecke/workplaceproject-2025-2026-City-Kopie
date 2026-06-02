// pages/api/[...path].ts
import type { NextApiRequest, NextApiResponse } from "next";
import { Readable } from "stream";
import { buffer } from "micro";

export const config = { api: { bodyParser: false } };

const BACKEND_BASE_URL =
  process.env.NEXT_PUBLIC_API_URL ||
  "https://back-end-team22-wpp-team-22.apps.okd.ucll.cloud";

// -----------------------------
// Helpers
// -----------------------------

// Keep body as Buffer – works for JSON *and* multipart
async function extractRawBody(req: NextApiRequest): Promise<Buffer> {
  return buffer(req);
}

function buildHeaders(req: NextApiRequest): Record<string, string> {
  const headers: Record<string, string> = {};

  // Forward original content-type if present
  if (req.headers["content-type"]) {
    headers["Content-Type"] = String(req.headers["content-type"]);
  }

  if (req.headers.authorization) {
    headers["Authorization"] = String(req.headers.authorization);
  }

  return headers;
}

function buildBackendUrl(req: NextApiRequest): string {
  const [path, query] = req.url?.split("?") || ["", ""];
  const endpoint = path.replace(/^\/api/, "");

  return `${BACKEND_BASE_URL}${endpoint}${query ? `?${query}` : ""}`;
}

async function forwardRequestToBackend(
  req: NextApiRequest,
  url: string,
  headers: Record<string, string>,
  rawBody: Buffer
) {
  const method = req.method || "GET";

  const hasBody =
    method === "POST" || method === "PUT" || method === "PATCH";

  // Cast to any so TS doesn't complain; runtime fetch accepts Buffer in Node.
  const body = hasBody ? (rawBody as any) : undefined;

  return fetch(url, {
    method,
    headers,
    body,
  });
}

function forwardHeaders(backendResponse: Response, res: NextApiResponse): void {
  const blacklist = ["content-encoding", "content-length", "transfer-encoding"];

  backendResponse.headers.forEach((value, key) => {
    if (!blacklist.includes(key.toLowerCase())) {
      res.setHeader(key, value);
    }
  });
}

function pipeResponseBody(
  backendResponse: Response,
  res: NextApiResponse
): void {
  if (!backendResponse.body) {
    res.end();
    return;
  }

  const webStream = backendResponse.body as any;
  const nodeStream = Readable.fromWeb(webStream);
  nodeStream.pipe(res);
}

// -----------------------------
// Main Handler
// -----------------------------

export default async function proxyHandler(
  req: NextApiRequest,
  res: NextApiResponse
) {
  try {
    const rawBody = await extractRawBody(req);
    const headers = buildHeaders(req);
    const backendUrl = buildBackendUrl(req);

    const backendResponse = await forwardRequestToBackend(
      req,
      backendUrl,
      headers,
      rawBody
    );

    res.status(backendResponse.status);
    forwardHeaders(backendResponse, res);
    pipeResponseBody(backendResponse, res);
  } catch (error) {
    console.error("Proxy error:", error);
    if (!res.headersSent) {
      res.status(500).json({ error: "Internal proxy error" });
    }
  }
}
