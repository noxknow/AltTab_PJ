import { requestExecutor, responseExecutor } from '@/types/executor';
import { API } from './api';

export const executor = {
  endpoint: {
    default: '/executor',
  },

  execute: async (form: requestExecutor): Promise<responseExecutor> => {
    const { data } = await API.post(
      `${executor.endpoint.default}/execute`,
      form,
    );
    return data;
  },

  getStatus: async (
    studyId: string,
    problemId: string,
    memberId: string,
  ): Promise<responseExecutor> => {
    const { data } = await API.get(
      `${executor.endpoint.default}/status/${studyId}/${problemId}/${memberId}`,
    );
    return data;
  },

  getCode: async (studyId: string, problemId: string, memberId: string) => {
    const { data } = await API.get(
      `${executor.endpoint.default}/${studyId}/${problemId}/${memberId}`,
    );
    return data;
  },
};
