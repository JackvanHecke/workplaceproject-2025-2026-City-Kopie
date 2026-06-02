import { Stakeholder } from "../types";

const apiUrl = "/api";

const getAllStakeholders = async (): Promise<Stakeholder[]> => {
  const response = await fetch(`${apiUrl}/stakeholders`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) throw new Error("Failed to fetch stakeholders");
  return response.json();
};

// NEW
const getStakeholdersForLocation = async (
  benchId: number
): Promise<Stakeholder[]> => {
  const response = await fetch(`${apiUrl}/locations/${benchId}/stakeholders`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) {
    throw new Error(`Failed to fetch stakeholders for bench ${benchId}`);
  }
  return response.json();
};

const createStakeholder = async (stakeholder: Stakeholder): Promise<Stakeholder> => {
  const response = await fetch(`${apiUrl}/stakeholders`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(stakeholder),
  });
  if (!response.ok) throw new Error("Failed to create stakeholder");
  return response.json();
};

const deleteStakeholder = async (id: number): Promise<void> => {
  const response = await fetch(`${apiUrl}/stakeholders/${id}`, {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
  });
  if (!response.ok) throw new Error("Failed to delete stakeholder");
};

const updateStakeholder = async (
  id: number,
  stakeholder: Partial<Stakeholder>
): Promise<Stakeholder> => {
  const response = await fetch(`${apiUrl}/stakeholders/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(stakeholder),
  });
  if (!response.ok) throw new Error("Failed to update stakeholder");
  return response.json();
};

const StakeholderService = {
  getAll: getAllStakeholders,
  getForLocation: getStakeholdersForLocation, // NEW
  create: createStakeholder,
  delete: deleteStakeholder,
  update: updateStakeholder,
};

export default StakeholderService;
