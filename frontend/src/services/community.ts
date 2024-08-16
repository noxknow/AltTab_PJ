import { communityStudy, weeklyStudies } from '@/types/study.ts';
import { API } from './api';

export const community = {
  endpoint: {
    default: '/community/main',
    solve: '/community/top/solve',
    follower: '/community/top/follower',
    follow: '/community/follow',
    myFollow: '/community/my/follow',
  },

  getWeeklyStudies: async (): Promise<weeklyStudies> => {
    const { data } = await API.get<weeklyStudies>(
      `${community.endpoint.default}`,
    );

    return data;
  },

  getTopSolvedStudies: async (): Promise<communityStudy[]> => {
    const { data } = await API.get<communityStudy[]>(
      `${community.endpoint.solve}`,
    );

    return data;
  },

  getTopFollowerStudies: async (): Promise<communityStudy[]> => {
    const { data } = await API.get<communityStudy[]>(
      `${community.endpoint.follower}`,
    );

    return data;
  },

  getFollowingStudies: async () => {
    const { data } = await API.get<communityStudy[]>(
      `${community.endpoint.myFollow}`,
    );

    return data;
  },

  follow: async (studyId: string) => {
    const { data } = await API.post(`${community.endpoint.follow}/${studyId}`);

    return data;
  },
};
