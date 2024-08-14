import { Attendance } from './Attendance';
import { WeeklyProblems } from './WeeklyProblems';
import styles from './StudyMain.module.scss';
import { ClickedDateProvider } from '@/contexts/clickedDate';

export function StudyMain() {
  return (
    <div className={styles.main}>
      <ClickedDateProvider>
        <WeeklyProblems />
        <Attendance />
      </ClickedDateProvider>
    </div>
  );
}
