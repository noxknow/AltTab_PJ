import { useQuery } from '@tanstack/react-query';
import { problem } from '@/services/searchProblem';
import { SearchProblemListResponse } from '@/types/problem';

const problemKeys = {
  search: ['search'],
};

export const useSearchProblemsQuery = (problemInfo: string) => {
    const { data, isLoading, refetch } = useQuery({
    queryKey: problemKeys.search,
    queryFn: (): Promise<SearchProblemListResponse> => problem.searchProblems(problemInfo),
    enabled: false,
  });

  return { data, isLoading, refetch };
};