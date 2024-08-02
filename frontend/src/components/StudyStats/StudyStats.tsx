import StudyStatChart from './StudyStatChart';
import styles from './StudyStats.module.scss';
import StudyStatsHeader from './StudyStatsHeader';
import StudyStatsInfo from './StudyStatsInfo';

export default function StudyStats() {
  return (
    <div className={styles.main}>
      <StudyStatsHeader />
      <StudyStatChart />
      <StudyStatsInfo score={5247} solved={141} ranking={11} />
    </div>
  );
}
