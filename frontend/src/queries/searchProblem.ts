import { useQuery } from '@tanstack/react-query';
import { problem } from '@/services/searchProblem';
import { SearchProblemListResponse } from '@/types/problem';

const problemKeys = {
  search: ['search'],
};

export const useSearchProblemsQuery = (problemInfo: string, page: number, size: number) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: [...problemKeys.search, problemInfo, page, size],
    queryFn: (): Promise<SearchProblemListResponse> => problem.searchProblems(problemInfo, page, size),
    enabled: false,
  });

  return { data, isLoading, refetch };
};