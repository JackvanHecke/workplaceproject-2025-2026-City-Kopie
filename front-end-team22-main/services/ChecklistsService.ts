import { userAgent } from "next/server";

const apiUrl = "/api"

const getAll = () => {

  return fetch('/api/checklists', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })
};

const markChecklistAsComplete = (checklistId: string, userId: string) => {
  if (!checklistId || !userId) {
    console.error("Missing checklistId or userId");
    return;
  }
  return fetch(`/api/checklists/${checklistId}/complete/${userId}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  });
};


const ChecklistsService = {
  getAll, markChecklistAsComplete
};

export default ChecklistsService;