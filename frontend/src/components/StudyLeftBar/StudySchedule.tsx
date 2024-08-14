import CalendarSVG from '@/assets/icons/calendar.svg?react';
import { differenceInDays } from 'date-fns';
import styles from './StudySchedule.module.scss';

type StudyScheduleProps = {
  date: string;
};

export function StudySchedule({ date }: StudyScheduleProps) {
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
        D - {differenceInDays(new Date(date), new Date()) + 1}
      </div>
    </div>
  );
}
