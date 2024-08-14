import { API } from './api';
import { SearchProblemListResponse } from '@/types/problem';

export const problem = {
  endpoint: {
    default: '/problem',
  },

  searchProblems: async (problemInfo: string): Promise<SearchProblemListResponse> => {
    const { data } = await API.get<SearchProblemListResponse>(
      `${problem.endpoint.default}/${problemInfo}`
    );
    
    return data;
  },
};