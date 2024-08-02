import { useParams } from 'react-router-dom';

import styles from './Study.module.scss';
import StudyLeftBar from '@/components/StudyLeftBar/StudyLeftBar';
import StudyStats from '@/components/StudyStats/StudyStats';
import StudyMain from '@/components/StudyMain/StudyMain';

export function Study() {
  const { studyId } = useParams();
  // return <div className={styles.container}>Study ID : {studyId}</div>;
  return (
    <div className={styles.main}>
      <StudyLeftBar />
      <StudyMain />
      <StudyStats />
    </div>
  );
}
