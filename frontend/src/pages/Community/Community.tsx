import { CommunityProfile } from '@/components/CommunityProfile/CommunityProfile';

import styles from './Community.module.scss';

export function Community() {
  return (
    <div className={styles.container}>
      <div className={styles.weeklyStudies}>
        <div>금주의 스터디</div>
        <div className={styles.cardContainer}>
          <CommunityProfile />
          <CommunityProfile />
          <CommunityProfile />
        </div>
      </div>
      <div className={styles.topStudies}>
        <div className={styles.filterContainer}>
          <div className={styles.filter}>푼 문제 수</div>
          <div className={styles.filter}>팔로잉 수</div>
        </div>
        <div className={styles.cardContainer}>
          <CommunityProfile />
          <CommunityProfile />
          <CommunityProfile />
          <CommunityProfile />
          <CommunityProfile />
          <CommunityProfile />
          <CommunityProfile />
        </div>
      </div>
    </div>
  );
}
