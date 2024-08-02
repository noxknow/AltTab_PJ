import { studyInfo } from '@/types/study.ts';
import { API } from './api';

export const study = {
  endpoint: {
    default: '/study',
  },

  create: async (form: studyInfo) => {
    await API.post(
      `${study.endpoint.default}`,
      form,
    );
  },

  loadInfo: async (studyId: string): Promise<studyInfo> => {
    const { data } = await API.get<studyInfo>(
      `${study.endpoint.default}/${studyId}`
    );

    return data;
  },
};
