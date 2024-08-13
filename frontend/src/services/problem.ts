import axios from 'axios';

import { URL } from '@/constants/url';
import { ProblemDetailsResponse } from '@/types/problem'; // 문제 상세 정보에 대한 타입 정의

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

  // 문제 상세 정보를 가져오는 함수
  getProblemDetails: async (
    problemId: number,
  ): Promise<ProblemDetailsResponse> => {
    const { data } = await problemAPI.get(
      `${problemService.endpoint.default}/problem/${problemId}`,
    );
    return data;
  },
};
