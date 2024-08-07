import { AttendanceIcon } from '../AttendanceIcon/AttendanceIcon';
import { Button } from '../Button/Button';
import styles from './Attendance.module.scss';
import Calendar from './Calendar';

export default function Attendance() {
  return (
    <div className={styles.main}>
      <div className={styles.top}>
        <div className={styles.header}>
          <div>
            <AttendanceIcon />
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
