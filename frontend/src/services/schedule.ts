import {
  deleteProblemForm,
  schedules,
  studyProblemDetails,
  studyProblemForm,
} from '@/types/schedule';
import { API } from './api';

export const schedule = {
  endpoint: {
    default: '/study/schedule',
  },

  postProblem: async (form: studyProblemForm) => {
    const { data } = await API.post(
      `${schedule.endpoint.default}/update`,
      form,
    );
    return data;
  },

  getSchedule: async (
    studyId: string,
    deadline: string,
  ): Promise<studyProblemDetails> => {
    const { data } = await API.get<studyProblemDetails>(
      `${schedule.endpoint.default}/${studyId}/${deadline}`,
    );
    return data;
  },

  deleteSchedule: async (studyId: string, deadline: string) => {
    const { data } = await API.delete(
      `${schedule.endpoint.default}/${studyId}/${deadline}`,
    );
    return data;
  },

  deleteProblem: async (form: deleteProblemForm) => {
    const { data } = await API.delete(
      `${schedule.endpoint.default}/problem/delete`,
      { data: form },
    );
    return data;
  },

  getSchedules: async (
    studyId: string,
    yearMonth: string,
  ): Promise<schedules> => {
    const { data } = await API.get<schedules>(
      `${schedule.endpoint.default}/deadline/${studyId}/${yearMonth}`,
    );
    return data;
  },

  getUpcomingSchedule: async (
    studyId: string,
  ): Promise<studyProblemDetails> => {
    const { data } = await API.get<studyProblemDetails>(
      `${schedule.endpoint.default}/recent/${studyId}`,
    );
    return data;
  },
};
