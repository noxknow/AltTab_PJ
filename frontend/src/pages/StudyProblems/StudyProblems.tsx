import StudyLeftBar from '@/components/StudyLeftBar/StudyLeftBar';
import styles from './StudyProblems.module.scss';
import { TotalProblemList } from '@/components/TotalProblemList/TotalProblemList';

export function StudyProblems() {
  return (
    <div className={styles.main}>
      <div className={styles.left}>
        <StudyLeftBar />
      </div>
      <div className={styles.right}>
        <TotalProblemList />
      </div>
    </div>
  );
}
