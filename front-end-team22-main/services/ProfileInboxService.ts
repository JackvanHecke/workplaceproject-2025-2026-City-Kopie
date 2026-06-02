const baseUrl = "/api/profiles";

export const ProfileInboxService = {
  getInbox: async (profileId: number, limit = 4) => {
    return fetch(`${baseUrl}/${profileId}/inbox?limit=${limit}`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });
  },

  markRead: async (profileId: number, inboxId: number) => {
    return fetch(`${baseUrl}/${profileId}/inbox/${inboxId}/read`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
    });
  },
};

export default ProfileInboxService;
