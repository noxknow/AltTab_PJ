import { useQuery } from '@tanstack/react-query';

import { community } from '@/services/community';
import { communityStudy, weeklyStudies } from '@/types/study.ts';

const studyKeys = {
  weekly: ['weekly'],
  solve: ['solve'],
  follower: ['follower'],
};

export const useGetWeeklyStudiesQuery = () => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: studyKeys.weekly,
    queryFn: (): Promise<weeklyStudies> => community.getWeeklyStudies(),
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
