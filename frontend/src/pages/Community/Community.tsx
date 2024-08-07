import { useState } from 'react';
import classNames from 'classnames';

import { CommunityProfile } from '@/components/CommunityProfile/CommunityProfile';
import { FILTER } from '@/constants/studyFilter';

import styles from './Community.module.scss';

export function Community() {
  const [filter, setFilter] = useState(FILTER.SOLVED);
  const solved = classNames(styles.filter, {
    [styles.selected]: filter === FILTER.SOLVED,
  });
  const follower = classNames(styles.filter, {
    [styles.selected]: filter === FILTER.FOLLOWER,
  });

  const handleFilterClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    setFilter(e.currentTarget.name);
  };

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
          <button name={FILTER.SOLVED} onClick={handleFilterClick}>
            <div className={solved}>푼 문제 수</div>
          </button>
          <button name={FILTER.FOLLOWER} onClick={handleFilterClick}>
            <div className={follower}>팔로잉 수</div>
          </button>
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
