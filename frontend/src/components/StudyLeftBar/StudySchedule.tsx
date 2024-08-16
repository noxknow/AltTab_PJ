import { differenceInDays, isSameDay } from 'date-fns';

import CalendarSVG from '@/assets/icons/calendar.svg?react';

import styles from './StudySchedule.module.scss';

type StudyScheduleProps = {
  date: string;
};

export function StudySchedule({ date }: StudyScheduleProps) {
  const diff = differenceInDays(new Date(date), new Date());

  return (
    <div className={styles.main}>
      <div className={styles.header}>
        <div>
          <CalendarSVG />
        </div>
        <div>Schedule</div>
      </div>
      <div className={styles.mid}>Next Study : {date}</div>
      <div className={styles.bottom}>
        D - {isSameDay(date, new Date()) ? 'Day' : diff + 1}
      </div>
    </div>
  );
}
