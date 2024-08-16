import StatsSVG from '@/assets/icons/stats.svg?react';
import styles from './StudyStatsHeader.module.scss';

export function StudyStatsHeader() {
  return (
    <div className={styles.main}>
      <div>
        <StatsSVG />
      </div>
      <div>통계 및 분석</div>
    </div>
  );
}
