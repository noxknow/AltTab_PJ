import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { useGetAttendances } from '@/queries/attendance';
import { useDeleteScheduleQuery } from '@/queries/schedule';
import { useClickedDate } from '@/hooks/useClickedDate';
import CloseSVG from '@/assets/icons/close.svg?react';

import styles from './AttendanceInfo.module.scss';

type EventData = {
  id: string;
  title: string;
  start: string;
};

type AttendanceInfoProps = {
  events: EventData[] | undefined;
  refetchSchedules: () => Promise<void>;
};

export function AttendanceInfo({
  events,
  refetchSchedules,
}: AttendanceInfoProps) {
  const { clickedDate } = useClickedDate();
  const { studyId } = useParams<{ studyId: string }>();
  const { mutateAsync: deleteScheduleMutation } = useDeleteScheduleQuery();
  const { refetch } = useGetAttendances(parseInt(studyId!), clickedDate);

  const [participants, setParticipants] = useState<string[]>();

  const refetchAttendances = useCallback(async () => {
    const { data } = await refetch();
    if (data) {
      setParticipants(data.members);
    }
  }, []);

  useEffect(() => {
    refetchAttendances();
  }, [clickedDate]);

  const handleDeleteSchedule = async () => {
    if (!confirm('일정을 삭제할까요?')) {
      return;
    }
    const updatedEvents = events?.filter(
      (event) => event.start === clickedDate,
    );
    if (updatedEvents?.length === 1) {
      await deleteScheduleMutation({
        studyId: studyId!,
        deadline: clickedDate,
      });
      await refetchSchedules();
      await refetchAttendances();
    }
  };

  return (
    <div className={styles.main}>
      <div className={styles.header}>
        {clickedDate}
        <button className={styles.closeButton} onClick={handleDeleteSchedule}>
          <CloseSVG width={30} height={30} stroke="#F24242" />
        </button>
      </div>
      <div className={styles.members}>
        {participants &&
          participants.map((participant, index) => (
            <div className={styles.member} key={index}>
              {participant}
            </div>
          ))}
      </div>
    </div>
  );
}
