import styles from './StudyStatsInfo.module.scss';

type StudyStatsInfoProps = {
  score: number;
  solved: number;
  ranking: number;
};

export function StudyStatsInfo({
  score,
  solved,
  ranking,
}: StudyStatsInfoProps) {
  function getOrdinalSuffix(ranking: number) {
    let suffix = 'th';
    if (ranking % 10 === 1 && ranking % 100 !== 11) {
      suffix = 'st';
    } else if (ranking % 10 === 2 && ranking % 100 !== 12) {
      suffix = 'nd';
    } else if (ranking % 10 === 3 && ranking % 100 !== 13) {
      suffix = 'rd';
    }
    return suffix;
  }
  return (
    <div className={styles.main}>
      <div className={styles.info}>
        <div className={styles.value}>{score.toLocaleString('ko-KR')}</div>
        <div className={styles.label}>Score</div>
      </div>
      <div className={styles.info}>
        <div className={styles.value}>{solved.toLocaleString('ko-KR')}</div>
        <div className={styles.label}>Solved</div>
      </div>
      <div className={styles.info}>
        <div className={styles.value}>
          {ranking.toLocaleString('ko-KR') + getOrdinalSuffix(ranking)}
        </div>
        <div className={styles.label}>Ranking</div>
      </div>
    </div>
  );
}
