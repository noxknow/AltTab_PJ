import { EventClickArg } from '@fullcalendar/core/index.js';
import { format } from 'date-fns';
import { useEffect, useState } from 'react';
import styles from './AttendanceInfo.module.scss';

type AttendanceInfoProps = {
  eventclickarg: EventClickArg;
};

export function AttendanceInfo({ eventclickarg }: AttendanceInfoProps) {
  const date = eventclickarg.event.start;

  const [participants, setParticipants] = useState<string[]>([]);
  useEffect(() => {
    setParticipants(eventclickarg.event.extendedProps.participants);
  }, [eventclickarg.event.extendedProps.participants]);

  return (
    <div className={styles.main}>
      <div className={styles.header}>
        {date ? format(date, 'yyyy-MM-dd') : ''}
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
