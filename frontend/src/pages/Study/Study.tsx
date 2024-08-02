import styles from './Study.module.scss';
import StudyLeftBar from '@/components/StudyLeftBar/StudyLeftBar';
import StudyStats from '@/components/StudyStats/StudyStats';
import StudyMain from '@/components/StudyMain/StudyMain';

export function Study() {
  return (
    <div className={styles.main}>
      <StudyLeftBar />
      <StudyMain />
      <StudyStats />
    </div>
  );
}
