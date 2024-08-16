import { useParams } from 'react-router-dom';

import { StudyLeftBar } from '@/components/StudyLeftBar/StudyLeftBar';
import { StudyStats } from '@/components/StudyStats/StudyStats';
import { StudyMain } from '@/components/StudyMain/StudyMain';
import { ClickedDateProvider } from '@/contexts/clickedDate';
import { StudyProvider } from '@/contexts/study';

import styles from './Study.module.scss';

export default function Study() {
  const { studyId } = useParams();

  return (
    <StudyProvider>
      <ClickedDateProvider>
        <div key={studyId} className={styles.main}>
          <StudyLeftBar />
          <StudyMain />
          <StudyStats />
        </div>
      </ClickedDateProvider>
    </StudyProvider>
  );
}
