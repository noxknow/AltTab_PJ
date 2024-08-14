import axios from 'axios';

import { URL } from '@/constants/url';
import { ProblemDetailsResponse } from '@/types/problem';

const baseURL = URL.RECOMMEND;
const headers = {
  'Content-Type': 'application/json',
  Accept: 'application/json',
};

const problemAPI = axios.create({
  baseURL,
  headers,
  withCredentials: true,
});

export const problemService = {
  endpoint: {
    default: '/flask',
  },

  getProblemDetails: async (
    problemId: number,
  ): Promise<ProblemDetailsResponse> => {
    const { data } = await problemAPI.get(
      `${problemService.endpoint.default}/problem/${problemId}`,
    );
    return data;
  },
};
