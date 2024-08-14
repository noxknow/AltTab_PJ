import { useCallback, useEffect, useState } from 'react';
import styles from './AttendanceInfo.module.scss';
import { useGetAttendances } from '@/queries/attendance';
import { useParams } from 'react-router-dom';
import { useClickedDate } from '@/hooks/useClickedDate';

export function AttendanceInfo() {
  const { clickedDate } = useClickedDate();
  const { studyId } = useParams<{ studyId: string }>();

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

  return (
    <div className={styles.main}>
      <div className={styles.header}>{clickedDate}</div>
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
