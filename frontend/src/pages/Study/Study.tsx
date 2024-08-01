import { useParams } from 'react-router-dom';

import styles from './Study.module.scss';
import StudyLeftBar from '@/components/StudyLeftBar/StudyLeftBar';

export function Study() {
  const { studyId } = useParams();
  // return <div className={styles.container}>Study ID : {studyId}</div>;
  return (
    <div>
      <StudyLeftBar />
    </div>
  );
}
