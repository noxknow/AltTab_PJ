import { useQuery, useMutation, QueryClient } from '@tanstack/react-query';

import { executor } from '@/services/executor';
import { requestExecutor } from '@/types/executor';

const executorKeys = {
  execute: ['execute'],
  status: ['status'],
  code: ['code'],
};

export const useExecuteQuery = () => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: (form: requestExecutor) => executor.execute(form),
    onSuccess: (_, form) => {
      queryClient.invalidateQueries({
        queryKey: [
          ...executorKeys.status,
          form.studyGroupId,
          form.problemId,
          form.problemTab,
        ],
      });
    },
  });
};

export const useGetExecutorStatusQuery = (
  studyId: string,
  problemId: string,
  problemTab: string,
) => {
  const { data, refetch } = useQuery({
    queryKey: [...executorKeys.status, studyId, problemId, problemTab],
    queryFn: () => executor.getStatus(studyId, problemId, problemTab),
  });
  return { data, refetch };
};

export const useGetCodeQuery = (
  studyId: string,
  problemId: string,
  problemTab: string,
) => {
  const { data, refetch } = useQuery({
    queryKey: [...executorKeys.status, studyId, problemId, problemTab],
    queryFn: () => executor.getCode(studyId, problemId, problemTab),
  });
  return { data, refetch };
};
