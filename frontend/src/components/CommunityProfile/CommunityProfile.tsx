import styles from '@/components/CommunityProfile/CommunityProfile.module.scss';
import FullHeartSVG from '@/assets/icons/full_heart.svg?react';
import { communityStudy } from '@/types/study';

export function CommunityProfile({ study }: { study: communityStudy }) {
  return (
    <div className={styles.profile_card}>
      <div className={styles.profile_card_left}>
        <div className={styles.profile_study}>{study.name}</div>
        <FullHeartSVG />
        <div className={styles.profile_detail}>{study.like}</div>
        <img
          className={styles.profile_image}
          src="https://fir-rollup.firebaseapp.com/de-sm.jpg"
          alt="No Image"
        />
      </div>
      <div className={styles.profile_card_right}>
        <div className={styles.profile_name}>Lana Steiner</div>
        <div className={styles.profile_detail}>18 Jan 2022</div>
      </div>
    </div>
  );
}
