import { useParams } from 'react-router-dom';

import { Attendance } from './Attendance';
import { WeeklyProblems } from './WeeklyProblems';
import styles from './StudyMain.module.scss';

export function StudyMain() {
  const { studyId } = useParams();
  return (
    <div className={styles.main} key={studyId}>
      <WeeklyProblems />
      <Attendance />
    </div>
  );
}
