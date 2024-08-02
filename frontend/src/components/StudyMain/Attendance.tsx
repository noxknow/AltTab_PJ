import { AttendanceIcon } from '../AttendanceIcon/AttendanceIcon';
import styles from './Attendance.module.scss';

export default function Attendance() {
  return (
    <div className={styles.main}>
      <div className={styles.header}>
        <div>
          <AttendanceIcon />
        </div>
        <div>출석부</div>
      </div>
      <div></div>
    </div>
  );
}
