import { StatsIcon } from '../StatsIcon/StatsIcon';
import styles from './StudyStatsHeader.module.scss';

export default function StudyStatsHeader() {
  return (
    <div className={styles.main}>
      <div>
        <StatsIcon />
      </div>
      <div>통계 및 분석</div>
    </div>
  );
}
