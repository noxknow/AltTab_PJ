import { problems } from '@/services/problems';
import { studyProblemsDetails } from '@/types/problems';
import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';

const problemsKeys = {
  solveProblem: ['solveProblem'],
  getProblems: ['getProblems'],
  getFilteredProblems: ['getFilteredProblems'],
};

export const useSolveProblemQuery = (
  memberId: number,
  studyId: number,
  problemId: number,
) => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: () => problems.solveProblem(memberId, studyId, problemId),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: problemsKeys.solveProblem,
      });
    },
  });
};

export const useGetProblemsQuery = (studyId: string) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: problemsKeys.getProblems,
    queryFn: (): Promise<studyProblemsDetails> => problems.getProblems(studyId),
  });
  return { data, isLoading, refetch };
};
export const useGetFilteredProblemsQuery = (
  studyId: string,
  option: number,
  target: string,
) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: problemsKeys.getFilteredProblems,
    queryFn: (): Promise<studyProblemsDetails> =>
      problems.getFilteredProblems(studyId, option, target),
  });
  return { data, isLoading, refetch };
};
