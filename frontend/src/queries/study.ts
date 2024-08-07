import { useQuery, useMutation, QueryClient } from '@tanstack/react-query';

import { study } from '@/services/study';
import { studyInfo, memberInfo } from '@/types/study.ts';

const studyKeys = {
  create: ['create'],
  studyInfo: ['studyInfo'],
  studyMember: ['studyMember'],
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
  const { data, isLoading } = useQuery({
    queryKey: studyKeys.studyInfo,
    queryFn: (): Promise<studyInfo> => study.getStudyInfo(studyId),
  });
  return { data, isLoading };
};

export const useGetStudyMemberQuery = (studyId: string) => {
  const { data, isLoading } = useQuery({
    queryKey: studyKeys.studyInfo,
    queryFn: (): Promise<memberInfo> => study.getStudyMember(studyId),
  });
  return { data, isLoading };
};
