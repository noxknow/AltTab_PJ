import AttendanceSVG from '@/assets/icons/attendance.svg?react';
import { Button } from '@/components/Button/Button';
import styles from './Attendance.module.scss';
import { Calendar } from './Calendar';

export function Attendance() {
  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div className={styles.header}>
          <div>
            <AttendanceSVG />
          </div>
          <div>출석부</div>
        </div>
        <div>
          <Button color="green" fill={true} size="small">
            출석하기
          </Button>
        </div>
      </div>
      <div>
        <Calendar />
      </div>
    </div>
  );
}
