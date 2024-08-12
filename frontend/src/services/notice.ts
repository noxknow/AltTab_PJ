import { API } from './api';

export const notice = {
  endpoint: {
    count: '/notification/count',
  },

  countNotification: async () => {
    const { data } = await API.get(`${notice.endpoint.count}`);

    return data;
  },
};
