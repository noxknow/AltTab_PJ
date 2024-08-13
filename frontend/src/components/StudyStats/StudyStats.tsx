import { useParams } from 'react-router-dom';

import { StudyStatChart } from './StudyStatChart';
import styles from './StudyStats.module.scss';
import { StudyStatsHeader } from './StudyStatsHeader';
import { StudyStatsInfo } from './StudyStatsInfo';

export function StudyStats() {
  const { studyId } = useParams();

  return (
    <div className={styles.main}>
      <StudyStatsHeader />
      <StudyStatChart studyId={studyId!} />
      <StudyStatsInfo studyId={studyId!} />
    </div>
  );
}
