import axios from 'axios';

import { URL } from '@/constants/url';
import {
  RecommendationRequest,
  RecommendationResponse,
} from '@/types/recommend';

const baseURL = URL.RECOMMEND;
const headers = {
  'Content-Type': 'application/json',
  Accept: 'application/json',
};

const recommendAPI = axios.create({
  baseURL,
  headers,
  withCredentials: true,
});

export const recommend = {
  endpoint: {
    default: '/flask',
  },

  // 추천 문제를 가져오는 함수
  getRecommendations: async (
    requestData: RecommendationRequest,
  ): Promise<RecommendationResponse> => {
    const { data } = await recommendAPI.post(
      `${recommend.endpoint.default}`,
      requestData,
    );
    return data;
  },
};
