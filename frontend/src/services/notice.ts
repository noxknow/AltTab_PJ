import { API } from './api';

import { NotificationListResponse, NotificationRequestDto } from '@/types/notice'; 

export const notice = {
  endpoint: {
    default: '/notification',
    count: '/notification/count',
  },

  countNotification: async () => {
    const { data } = await API.get(`${notice.endpoint.count}`);

    return data;
  },

  getNotification: async (): Promise<NotificationListResponse> => {
    const { data } = await API.get<NotificationListResponse>(`${notice.endpoint.default}`);

    return data;
  },

  checkNotification: async (notificationRequestDto: NotificationRequestDto) => {
    await API.post(notice.endpoint.default, notificationRequestDto);
  },
};
