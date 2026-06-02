const getAll = () => {

  return fetch('/api/phases', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })
};


const PhaseService = {
  getAll
};

export default PhaseService;