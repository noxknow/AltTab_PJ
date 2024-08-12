import { memberInfo, joinedStudies, searchedMembers } from '@/types/study.ts';
import { API } from './api';

export const member = {
  endpoint: {
    default: '/member',
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

  getMembersByName: async (name: string) => {
    const { data } = await API.get<searchedMembers>(
      `${member.endpoint.default}/${name}`,
    );

    return data;
  },
};
