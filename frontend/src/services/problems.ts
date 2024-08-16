import { studyProblemsDetails } from '@/types/problems';
import { API } from './api';

export const problems = {
  endpoint: {
    default: '/problem',
    problems: '/study/problems',
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
  getProblems: async (studyId: string): Promise<studyProblemsDetails> => {
    const { data } = await API.get<studyProblemsDetails>(
      `${problems.endpoint.problems}/${studyId}`,
    );
    return data;
  },
  getFilteredProblems: async (
    studyId: string,
    option: number,
    target: string,
  ): Promise<studyProblemsDetails> => {
    const { data } = await API.get<studyProblemsDetails>(
      `${problems.endpoint.default}/${studyId}/${option}/${target}`,
    );
    return data;
  },
};
