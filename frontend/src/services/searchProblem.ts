import { API } from './api';
import { SearchProblemListResponse } from '@/types/problem';

export const problem = {
  endpoint: {
    default: '/problem/search',
  },

  searchProblems: async (problemInfo: string, page: number, size: number): Promise<SearchProblemListResponse> => {
    const { data } = await API.get<SearchProblemListResponse>(
      `${problem.endpoint.default}/${problemInfo}`,
      {
        params: {
          page,
          size,
        },
      }
    );
    
    return data;
  },
};