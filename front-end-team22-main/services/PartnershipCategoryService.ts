import { PartnershipCategory } from "../types";

const apiUrl = "/api";

const getAll = async (): Promise<PartnershipCategory[]> => {
  const res = await fetch(`${apiUrl}/partnership-categories`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });
  if (!res.ok) throw new Error("Failed to fetch partnership categories");
  return res.json();
};

export default { getAll };
