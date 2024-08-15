import { Attendance } from './Attendance';
import { WeeklyProblems } from './WeeklyProblems';
import styles from './StudyMain.module.scss';

export function StudyMain() {
  return (
    <div className={styles.main}>
        <WeeklyProblems />
        <Attendance />
    </div>
  );
}
