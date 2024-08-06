import { requestExecutor } from '@/types/executor';
import { API } from './api';

export const executor = {
  endpoint: {
    default: '/executor',
  },

  execute: async (form: requestExecutor) => {
    const { data } = await API.post(
      `${executor.endpoint.default}/execute`,
      form,
    );
    return data;
  },

  status: async (studyId: string, problemId: string, problemTab: string) => {
    const { data } = await API.get(
      `${executor.endpoint.default}/status/${studyId}/${problemId}/${problemTab}`,
    );
    return data;
  },
};
