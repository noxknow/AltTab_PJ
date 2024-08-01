import { CalendarIcon } from '@/components/CalendarIcon/CalendarIcon';
import { format, differenceInDays } from 'date-fns';
import styles from './StudySchedule.module.scss';

type StudyScheduleProps = {
  date: Date;
};

export default function StudySchedule({ date }: StudyScheduleProps) {
  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div>
          <CalendarIcon />
        </div>
        <div>Schedule</div>
      </div>
      <div className={styles.mid}>
        Next Study : {format(date, 'yyyy-MM-dd h:mm a')}
      </div>
      <div className={styles.bottom}>
        D - {differenceInDays(date, new Date())}
      </div>
    </div>
  );
}
