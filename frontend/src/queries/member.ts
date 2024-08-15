import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

import { member } from '@/services/member';
import { memberInfo, joinedStudies, searchedMembers } from '@/types/study.ts';

const memberKeys = {
  studies: ['studies'],
  info: ['info'],
  logout: ['logout'],
  name: ['name'],
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
    refetchOnMount: false,
    refetchOnWindowFocus: false,
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

export const useGetMembersByNameQuery = (name: string) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: memberKeys.name,
    queryFn: (): Promise<searchedMembers> => member.getMembersByName(name),
    enabled: false,
  });
  return { data, isLoading, refetch };
};
