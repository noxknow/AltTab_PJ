import { StudyLeftBar } from '@/components/StudyLeftBar/StudyLeftBar';
import { TotalProblemList } from '@/components/TotalProblemList/TotalProblemList';
import { ClickedDateProvider } from '@/contexts/clickedDate';

import styles from './StudyProblems.module.scss';

export function StudyProblems() {
  return (
    <ClickedDateProvider>
      <div className={styles.main}>
        <div className={styles.left}>
          <StudyLeftBar />
        </div>
        <div className={styles.right}>
          <TotalProblemList />
        </div>
      </div>
    </ClickedDateProvider>
  );
}
