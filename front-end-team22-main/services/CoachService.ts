const getAll = () => {
  const apiUrl = "/api";
  return fetch('/api/coaches', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

const getById = (id: number | string) => {
  const apiUrl = "/api";
  return fetch(`/api/coaches/${id}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
};

const CoachService = {
  getAll,
  getById,
};

export default CoachService;
