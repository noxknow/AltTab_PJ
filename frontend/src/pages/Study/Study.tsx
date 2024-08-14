import { useParams } from 'react-router-dom';

import { StudyLeftBar } from '@/components/StudyLeftBar/StudyLeftBar';
import { StudyStats } from '@/components/StudyStats/StudyStats';
import { StudyMain } from '@/components/StudyMain/StudyMain';

import styles from './Study.module.scss';

export function Study() {
  const { studyId } = useParams();

  return (
    <div key={studyId} className={styles.main}>
      <StudyLeftBar />
      <StudyMain />
      <StudyStats />
    </div>
  );
}
