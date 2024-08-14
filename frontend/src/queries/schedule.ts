import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { schedule } from '@/services/schedule';
import {
  deleteProblemForm,
  schedules,
  studyProblemDetails,
  studyProblemForm,
} from '@/types/schedule';

const scheduleKeys = {
  postProblem: ['postProblem'],
  getSchedule: ['getSchedule'],
  deleteSchedule: ['deleteSchedule'],
  deleteProblem: ['deleteProblem'],
  getSchedules: ['getSchedules'],
  getUpcomingSchedule: ['getUpcomingSchedule'],
};

export const usePostProblemQuery = () => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: (form: studyProblemForm) => schedule.postProblem(form),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: scheduleKeys.postProblem,
      });
    },
  });
};

export const useGetScheduleQuery = (studyId: string, deadline: string) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: scheduleKeys.getSchedule,
    queryFn: (): Promise<studyProblemDetails> =>
      schedule.getSchedule(studyId, deadline),
  });
  return { data, isLoading, refetch };
};

export const useDeleteScheduleQuery = () => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: ({
      studyId,
      deadline,
    }: {
      studyId: string;
      deadline: string;
    }) => schedule.deleteSchedule(studyId, deadline),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: scheduleKeys.deleteSchedule,
      });
    },
  });
};
export const useDeleteProblemQuery = () => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: (form: deleteProblemForm) => schedule.deleteProblem(form),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: scheduleKeys.deleteProblem,
      });
    },
  });
};

export const useGetSchedulesQuery = (studyId: string, yearMonth: string) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: scheduleKeys.getSchedules,
    queryFn: (): Promise<schedules> =>
      schedule.getSchedules(studyId, yearMonth),
  });
  return { data, isLoading, refetch };
};

export const useGetUpcomingScheduleQuery = () => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: scheduleKeys.getUpcomingSchedule,
    queryFn: (): Promise<studyProblemDetails> => schedule.getUpcomingSchedule(),
  });
  return { data, isLoading, refetch };
};
