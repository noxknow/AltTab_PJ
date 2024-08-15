import { NavLink } from 'react-router-dom';
import classNames from 'classnames';

import { useGetMyStudiesQuery } from '@/queries/member';
import StudySVG from '@/assets/icons/study.svg?react';
import TodayStudySVG from '@/assets/icons/todayStudy.svg?react';
import RecommendSVG from '@/assets/icons/recommend.svg?react';
import ListSVG from '@/assets/icons/list.svg?react';

import styles from './Header.module.scss';

type HeaderSidebarProps = {
  isVisible: boolean;
  hideSidebar: () => void;
};

export function HeaderSidebar({ isVisible, hideSidebar }: HeaderSidebarProps) {
  const { data } = useGetMyStudiesQuery();

  const sibebarClass = classNames(styles.headerSidebar, {
    [styles.visible]: isVisible,
    [styles.hidden]: !isVisible,
  });

  return (
    <div className={styles.headerSidebarContainer} onMouseLeave={hideSidebar}>
      <div className={sibebarClass}>
        <div className={styles.itemWrapper}>
          <div className={styles.sidebarItem}>
            <StudySVG /> 내 스터디
            <div className={styles.myStudies}>
              {data &&
                data.joinedStudies &&
                data.joinedStudies.map((study, index) => (
                  <NavLink to={`study/${study.studyId}`} key={index}>
                    <div className={styles.myStudy}>{study.studyName}</div>
                  </NavLink>
                ))}
            </div>
          </div>
          <NavLink to={'community'}>
            <div className={styles.sidebarItem}>
              <TodayStudySVG /> 금주의 스터디
            </div>
          </NavLink>
          <div className={styles.sidebarItem}>
            <RecommendSVG /> 문제 추천
          </div>
          <div className={styles.sidebarItem}>
            <ListSVG /> 전체 문제리스트
          </div>
        </div>
      </div>
    </div>
  );
}
