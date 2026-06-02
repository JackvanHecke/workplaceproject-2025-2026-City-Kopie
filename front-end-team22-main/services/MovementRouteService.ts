// services/MovementRouteService.ts
import { MovementRoute } from "../types";

const apiUrl = "/api/movement-routes";

const getAll = (): Promise<MovementRoute[]> => {
  return fetch(apiUrl, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  }).then((res) => res.json());
};

const getById = (id: number | string): Promise<MovementRoute> => {
  return fetch(`${apiUrl}/${id}`, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
  }).then((res) => res.json());
};

const create = (payload: Partial<MovementRoute>): Promise<MovementRoute> => {
  return fetch(apiUrl, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then((res) => res.json());
};

const update = (
  id: number | string,
  payload: Partial<MovementRoute>
): Promise<MovementRoute> => {
  return fetch(`${apiUrl}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  }).then((res) => res.json());
};

const MovementRouteService = {
  getAll,
  getById,
  create,
  update,
};

export default MovementRouteService;
