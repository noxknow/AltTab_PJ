import { useQuery, useMutation, QueryClient } from '@tanstack/react-query';

import { study } from '@/services/study';
import { studyInfo, memberInfo, studyScore } from '@/types/study.ts';

const studyKeys = {
  create: ['create'],
  studyInfo: ['studyInfo'],
  studyMember: ['studyMember'],
  studyScore: ['studyScore'],
};

export const useCreateStudyQuery = () => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: (form: studyInfo) => study.create(form),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: studyKeys.create,
      });
    },
  });
};

export const useGetStudyInfoQuery = (studyId: string) => {
  const { data, isLoading, isError } = useQuery({
    queryKey: studyKeys.studyInfo,
    queryFn: (): Promise<studyInfo> => study.getStudyInfo(studyId),
    retry: false,
    retryOnMount: false,
    throwOnError: true,
  });

  return { data, isLoading, isError };
};

export const useGetStudyMemberQuery = (studyId: string) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: studyKeys.studyMember,
    queryFn: (): Promise<memberInfo[]> => study.getStudyMember(studyId),
    retry: false,
    retryOnMount: false,
  });

  return { data, isLoading, refetch };
};

export const useGetStudyScoreQuery = (studyId: string) => {
  const { data, isLoading } = useQuery({
    queryKey: studyKeys.studyScore,
    queryFn: (): Promise<studyScore> => study.getStudyScore(studyId),
    retry: false,
    retryOnMount: false,
  });

  return { data, isLoading };
};
