import { attendance } from '@/services/attendance';
import { attendanceInfo } from '@/types/attendance';
import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';

const attendanceKeys = {
  postAttendance: ['postAttendance'],
  getAttendances: ['getAttendances'],
};

export const usePostAttendanceQuery = (studyId: number, todayDate: string) => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: () => attendance.postAttendance(studyId, todayDate),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: attendanceKeys.postAttendance,
      });
    },
  });
};
export const useGetAttendances = (studyId: number, todayDate: string) => {
  const { data, isLoading, refetch } = useQuery({
    queryKey: attendanceKeys.getAttendances,
    queryFn: (): Promise<attendanceInfo> =>
      attendance.getAttendances(studyId, todayDate),
  });
  return { data, isLoading, refetch };
};
