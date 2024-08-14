import { problems } from '@/services/problems';
import { QueryClient, useMutation } from '@tanstack/react-query';

const problemsKeys = {
  solveProblem: ['solveProblem'],
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
