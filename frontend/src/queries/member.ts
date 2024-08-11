import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

import { member } from '@/services/member';
import { memberInfo, joinedStudies } from '@/types/study.ts';

const memberKeys = {
  studies: ['studies'],
  info: ['info'],
  logout: ['logout'],
};

export const useGetMyInfoQuery = () => {
  const { data, isLoading, isSuccess } = useQuery({
    queryKey: memberKeys.studies,
    queryFn: (): Promise<memberInfo> => member.getMemberInfo(),
  });
  const isLogin = isSuccess && data;
  return { data, isLoading, isLogin };
};

export const useGetMyStudiesQuery = () => {
  const { data, isLoading } = useQuery({
    queryKey: memberKeys.info,
    queryFn: (): Promise<joinedStudies> => member.getMemberStudies(),
  });
  return { data, isLoading };
};

export const useLogoutQuery = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: () => member.logout(),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: memberKeys.logout,
      });
    },
  });
};
