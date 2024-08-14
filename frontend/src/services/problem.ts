import axios from 'axios';
import { URL } from '@/constants/url';

const problemAPI = axios.create({
  baseURL: URL.RECOMMEND,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
  withCredentials: true,
});

export const problemService = {
  endpoint: {
    default: '/flask',
  },

  getProblemDetails: async (problemId: number): Promise<string> => {
    const { data } = await problemAPI.get(
      `${problemService.endpoint.default}/problem/${problemId}`,
    );
    return data;
  },
};

