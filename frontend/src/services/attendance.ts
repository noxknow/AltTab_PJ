import { attendanceInfo } from '@/types/attendance';
import { API } from './api';

export const attendance = {
  endpoint: {
    default: '/study/attend',
  },
  postAttendance: async (studyId: number, todayDate: string) => {
    const { data } = await API.post(
      `${attendance.endpoint.default}/${studyId}/${todayDate}`,
    );
    return data;
  },

  getAttendances: async (
    studyId: number,
    todayDate: string,
  ): Promise<attendanceInfo> => {
    const { data } = await API.get<attendanceInfo>(
      `${attendance.endpoint.default}/${studyId}/${todayDate}`,
    );
    return data;
  },
};
