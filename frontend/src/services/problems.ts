import { API } from './api';

export const problems = {
  endpoint: {
    default: '/problem',
  },
  solveProblem: async (
    memberId: number,
    studyId: number,
    problemId: number,
  ) => {
    const { data } = await API.post(
      `${problems.endpoint.default}/solve/${memberId}/${studyId}/${problemId}`,
    );
    return data;
  },
};
