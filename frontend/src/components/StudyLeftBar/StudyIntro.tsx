import { studyInfo } from '@/types/study.ts';

import styles from './StudyIntro.module.scss';

export function StudyIntro({ studyName, studyDescription }: studyInfo) {
  return (
    <div className={styles.main}>
      <div className={styles.name}>{studyName}</div>
      <div className={styles.info}>{studyDescription}</div>
    </div>
  );
}
