import { useParams } from 'react-router-dom';

import { useDeleteScheduleQuery } from '@/queries/schedule';
import { useClickedDate } from '@/hooks/useClickedDate';
import CloseSVG from '@/assets/icons/close.svg?react';
import { useStudyState } from '@/hooks/useStudyState';

import styles from './AttendanceInfo.module.scss';

type EventData = {
  id: string;
  title: string;
  start: string;
};

type AttendanceInfoProps = {
  events: EventData[] | undefined;
  refetchSchedules: () => Promise<void>;
  participants: string[] | undefined;
  refetchAttendances: () => Promise<void>;
};

export function AttendanceInfo({
  events,
  refetchSchedules,
  participants,
  refetchAttendances,
}: AttendanceInfoProps) {
  const { clickedDate } = useClickedDate();
  const { studyId } = useParams<{ studyId: string }>();
  const { mutateAsync: deleteScheduleMutation } = useDeleteScheduleQuery();
  const { isMember } = useStudyState();

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
        {isMember && (
          <button className={styles.closeButton} onClick={handleDeleteSchedule}>
            <CloseSVG width={30} height={30} stroke="#F24242" />
          </button>
        )}
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
