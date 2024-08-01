import { requestCompiler } from '@/types/compiler';
import { API } from './api';

export const compiler = {
  endpoint: {
    default: '/executor',
  },

  execute: async (form: requestCompiler) => {
    const { data } = await API.post(
      `${compiler.endpoint.default}/execute`,
      form,
    );
    return data;
  },

  status: async (studyId: string, problemId: string, problemTab: string) => {
    const { data } = await API.get(
      `${compiler.endpoint.default}/status/${studyId}/${problemId}/${problemTab}`,
    );
    return data;
  },
};
