import { blockInfo } from '@/types/solution';
import { API } from './api';

export const solutions = {
  endpoint: {
    default: '/solutions',
  },
  create: async (studyId: string, problemId: string, form: blockInfo) => {
    const { data } = await API.post(
      `${solutions.endpoint.default}/${studyId}/${problemId}`,
      form,
    );

    return data;
  },
  getBlocks: async (studyId: string, problemId: string): Promise<blockInfo> => {
    const { data } = await API.get<blockInfo>(
      `${solutions.endpoint.default}/${studyId}/${problemId}`,
    );

    return data;
  },
};
