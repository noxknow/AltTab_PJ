import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { format } from 'date-fns';

import AttendanceSVG from '@/assets/icons/attendance.svg?react';
import { Button } from '@/components/Button/Button';
import {
  useGetAttendances,
  usePostAttendanceQuery,
} from '@/queries/attendance';
import { useClickedDate } from '@/hooks/useClickedDate';
import { useStudyState } from '@/hooks/useStudyState';

import styles from './Attendance.module.scss';
import { Calendar } from './Calendar';

export function Attendance() {
  const { studyId } = useParams<{ studyId: string }>();
  const today = format(new Date(), 'yyyy-MM-dd');
  const { clickedDate } = useClickedDate();
  const [attendIsAble, setAttendIsAble] = useState(false);
  const [participants, setParticipants] = useState<string[]>();
  const { refetch } = useGetAttendances(parseInt(studyId!), clickedDate);
  const usePostAttendanceQueryMutation = usePostAttendanceQuery(
    parseInt(studyId!),
    today,
  );
  const { isMember } = useStudyState();

  const refetchAttendances = useCallback(async () => {
    const { data } = await refetch();
    if (data) {
      setParticipants(data.members);
      setAttendIsAble(data.attendCheck && clickedDate === today);
    }
  }, [clickedDate]);

  useEffect(() => {
    refetchAttendances();
  }, [clickedDate]);

  const handleClick = async () => {
    const result = await usePostAttendanceQueryMutation.mutateAsync();
    if (result) {
      const { members, attendCheck } = result;
      setParticipants(members);
      setAttendIsAble(attendCheck);
    }
  };

  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div className={styles.header}>
          <div>
            <AttendanceSVG />
          </div>
          <div>출석부</div>
        </div>
        {isMember && (
          <Button
            onClick={handleClick}
            color="green"
            fill={true}
            size="small"
            disabled={!attendIsAble}
          >
            출석하기
          </Button>
        )}
      </div>
      <Calendar
        participants={participants}
        refetchAttendances={refetchAttendances}
      />
    </div>
  );
}
