import { requestCompiler } from '@/types/compiler';
import { API } from './api';

export const compiler = {
  endpoint: {
    default: '/compiler',
  },

  execute: async (form: requestCompiler) => {
    const { data } = await API.post(
      `${compiler.endpoint.default}/execute`,
      form,
    );
    return data;
  },
};
