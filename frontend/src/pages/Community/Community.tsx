import { useState, useEffect } from 'react';
import classNames from 'classnames';

import { CommunityProfile } from '@/components/CommunityProfile/CommunityProfile';
import { FILTER } from '@/constants/studyFilter';
import {
  useGetWeeklyStudiesQuery,
  useGetTopSolvedStudiesQuery,
  useGetTopFollowerStudiesQuery,
} from '@/queries/community';
import { communityStudy } from '@/types/study';

import styles from './Community.module.scss';

export function Community() {
  const [filter, setFilter] = useState(FILTER.SOLVED);
  const [weeklyStudies, setWeeklyStudies] = useState<communityStudy[]>([]);
  const [communityStudies, setCommunityStudies] = useState<communityStudy[]>(
    [],
  );
  const solved = classNames(styles.filter, {
    [styles.selected]: filter === FILTER.SOLVED,
  });
  const follower = classNames(styles.filter, {
    [styles.selected]: filter === FILTER.FOLLOWER,
  });
  const { data: initialStudies } = useGetWeeklyStudiesQuery();
  const { refetch: getTopSolvedStudies } = useGetTopSolvedStudiesQuery();
  const { refetch: getTopFollowerStudies } = useGetTopFollowerStudiesQuery();

  useEffect(() => {
    const { weeklyStudies, topSolvers } = initialStudies
      ? initialStudies
      : { weeklyStudies: [], topSolvers: [] };
    setWeeklyStudies(weeklyStudies);
    setCommunityStudies(topSolvers);
  }, [initialStudies]);

  useEffect(() => {
    (async () => {
      const { data } =
        filter === FILTER.SOLVED
          ? await getTopSolvedStudies()
          : await getTopFollowerStudies();
      setCommunityStudies(data!);
    })();
  }, [filter]);

  const handleFilterClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    setFilter(e.currentTarget.name);
  };

  return (
    <div className={styles.container}>
      <div className={styles.weeklyStudies}>
        <div>금주의 스터디</div>
        <div className={styles.cardContainer}>
          {weeklyStudies &&
            weeklyStudies.map((study, index) => (
              <CommunityProfile key={index} study={study} />
            ))}
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
          {communityStudies &&
            communityStudies.map((study, index) => (
              <CommunityProfile key={index} study={study} />
            ))}
        </div>
      </div>
    </div>
  );
}
