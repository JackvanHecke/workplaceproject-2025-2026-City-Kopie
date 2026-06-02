import { Workshop } from "../types";

const apiUrl = "/api";

const getAllWorkshops = async (): Promise<Workshop[]> => {
  const response = await fetch(`/api/workshops`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) {
    throw new Error("Failed to fetch workshops");
  }

  return response.json();
};

const WorkshopService = {
  getAllWorkshops,
};

export default WorkshopService;
