import { useQuery, useMutation, QueryClient } from '@tanstack/react-query';

import { community } from '@/services/community';
import { communityStudy, weeklyStudies } from '@/types/study.ts';

const studyKeys = {
  weekly: ['weekly'],
  solve: ['solve'],
  follower: ['follower'],
  following: ['following'],
  follow: ['follow'],
};

export const useGetWeeklyStudiesQuery = () => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: studyKeys.weekly,
    queryFn: (): Promise<weeklyStudies> => community.getWeeklyStudies(),
    refetchOnMount: false,
    refetchOnWindowFocus: false,
  });
  return { data, isLoading, refetch };
};

export const useGetTopSolvedStudiesQuery = () => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: studyKeys.solve,
    queryFn: (): Promise<communityStudy[]> => community.getTopSolvedStudies(),
    enabled: false,
  });
  return { data, isLoading, refetch };
};

export const useGetTopFollowerStudiesQuery = () => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: studyKeys.follower,
    queryFn: (): Promise<communityStudy[]> => community.getTopFollowerStudies(),
    enabled: false,
  });
  return { data, isLoading, refetch };
};

export const useGetFollowingStudiesQuery = () => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: studyKeys.following,
    queryFn: (): Promise<communityStudy[]> => community.getFollowingStudies(),
    enabled: false,
  });
  return { data, isLoading, refetch };
};

export const useFollowQuery = () => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: (studyId: string) => community.follow(studyId),
    onSuccess: (_, studyId) => {
      queryClient.invalidateQueries({
        queryKey: [...studyKeys.follow, studyId],
      });
    },
  });
};
