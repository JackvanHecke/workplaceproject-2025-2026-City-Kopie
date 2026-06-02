// services/CommunicationService.ts
import type {
  CommunicationMessageDTO,
  CommunicationMessageCreateDTO,
  Profile, // add this import
} from "../types";

const baseUrl = "/api/communications";

const getAll = () => {
  return fetch(baseUrl, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
};

const getById = (id: number | string) => {
  return fetch(`${baseUrl}/${id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
};

const create = (payload: CommunicationMessageCreateDTO) => {
  return fetch(baseUrl, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });
};

const update = (id: number | string, payload: CommunicationMessageCreateDTO) => {
  return fetch(`${baseUrl}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });
};

const remove = (id: number | string) => {
  return fetch(`${baseUrl}/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  });
};

// For end-user / coach to get their active messages
const getActiveForProfile = (profileId: number | string) => {
  return fetch(`${baseUrl}/profile/${profileId}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
};

// ➜ NEW: lightweight profiles for recipient picker
const getRecipientProfiles = () => {
  return fetch("/api/profile/simple", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });
};

const CommunicationService = {
  getAll,
  getById,
  create,
  update,
  remove,
  getActiveForProfile,
  getRecipientProfiles,
};

export default CommunicationService;
  