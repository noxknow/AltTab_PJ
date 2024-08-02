import { studyInfo } from '@/types/study.ts';
import { API } from './api';

export const study = {
  endpoint: {
    default: '/study',
  },

  create: async (form: studyInfo) => {
    const { data } = await API.post(
      `${study.endpoint.default}`,
      form,
    );

    return data;
  },

  loadInfo: async (studyId: string): Promise<studyInfo> => {
    const { data } = await API.get<studyInfo>(
      `${study.endpoint.default}/${studyId}`
    );

    return data;
  },

  lodaMember: async (studyId: string): Promise<studyInfo> => {
    const { data } = await API.get<studyInfo>(
      `${study.endpoint.default}/${studyId}`
    );

    return data;
  },
};
