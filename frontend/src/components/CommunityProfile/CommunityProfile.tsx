import { NavLink } from 'react-router-dom';

import styles from '@/components/CommunityProfile/CommunityProfile.module.scss';
import FullHeartSVG from '@/assets/icons/full_heart.svg?react';
import { communityStudy } from '@/types/study';

export function CommunityProfile({ study }: { study: communityStudy }) {
  return (
    <NavLink to={`/study/${study.studyId}`} className={styles.profile_card}>
      <div className={styles.profile_card_title}>
        <div className={styles.profile_study}>{study.name}</div>
        <div className={styles.like}>
          <FullHeartSVG />
          <div className={styles.profile_detail}>{study.like}</div>
        </div>
      </div>
      <div className={styles.profile_card_body}>
        <img
          className={styles.profile_image}
          src={study.leaderMemberDto.avatarUrl}
          alt="Study Leader"
        />
        <div className={styles.description}>
          <div className={styles.profile_name}>
            {study.leaderMemberDto.name}
          </div>
          <div className={styles.profile_detail}>{study.studyDescription}</div>
        </div>
      </div>
    </NavLink>
  );
}
