import { memberInfo, joinedStudies } from '@/types/study.ts';
import { API } from './api';

export const member = {
  endpoint: {
    studies: '/member/studies',
    info: '/member/info',
    logout: '/member/logout',
  },

  getMemberInfo: async (): Promise<memberInfo> => {
    const { data } = await API.get<memberInfo>(`${member.endpoint.info}`);

    return data;
  },

  getMemberStudies: async (): Promise<joinedStudies> => {
    const { data } = await API.get<joinedStudies>(`${member.endpoint.studies}`);

    return data;
  },

  logout: async () => {
    await API.post(`${member.endpoint.logout}`);
  },
};
