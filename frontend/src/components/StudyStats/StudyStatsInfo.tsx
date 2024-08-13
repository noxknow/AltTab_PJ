import styles from './StudyStatsInfo.module.scss';

import { useGetStudyScoreQuery } from '@/queries/study';

type StudyStatsInfoProps = {
  studyId: string;
};

export function StudyStatsInfo({ studyId }: StudyStatsInfoProps) {
  const { data: scoreData } = useGetStudyScoreQuery(studyId);

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
        <div className={styles.value}>{scoreData?.totalScore.toLocaleString('ko-KR')}</div>
        <div className={styles.label}>Score</div>
      </div>
      <div className={styles.info}>
        <div className={styles.value}>{scoreData?.solveCount.toLocaleString('ko-KR')}</div>
        <div className={styles.label}>Solved</div>
      </div>
      <div className={styles.info}>
        <div className={styles.value}>
          {scoreData?.rank.toLocaleString('ko-KR') + getOrdinalSuffix(scoreData?.rank || 0)}
        </div>
        <div className={styles.label}>Ranking</div>
      </div>
    </div>
  );
}
