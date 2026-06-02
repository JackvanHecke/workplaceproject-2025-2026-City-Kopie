import { FAQ } from "@types";

const getAll = async (): Promise<FAQ[]> => {
  const res = await fetch("/api/FAQ", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  });

  return res.json();
};

const create = async (data: FAQ): Promise<Response> => {
  const res = await fetch("/api/FAQ", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
  return res;
};

const remove = async (id: number): Promise<Response> => {
  const res = await fetch(`/api/FAQ/${id}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  });
  return res;
};

const FAQService = {
  getAll,
  create,
  remove,
};

export default FAQService;
