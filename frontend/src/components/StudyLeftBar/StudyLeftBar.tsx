import StudyIntro from './StudyIntro';
import StudyMembers from './StudyMembers';
import StudySchedule from './StudySchedule';

import styles from './StudyLeftBar.module.scss';

export default function StudyLeftBar() {
  return (
    <div className={styles.main}>
      <StudyIntro
        name="SSAFY 최강 알고리즘 스터디"
        intro="서울 3반 알고리즘 스터디입니다."
      />
      <StudySchedule date={new Date('2024-08-05 20:00:00')} />
      <StudyMembers />
    </div>
  );
}
