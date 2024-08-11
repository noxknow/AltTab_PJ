import { NavLink } from 'react-router-dom';
import classNames from 'classnames';

import { useGetMyStudiesQuery } from '@/queries/member';

import styles from './Header.module.scss';

type HeaderSidebarProps = {
  isVisible: boolean;
  showSidebar: () => void;
  hideSidebar: () => void;
};

export function HeaderSidebar({
  isVisible,
  showSidebar,
  hideSidebar,
}: HeaderSidebarProps) {
  const { data } = useGetMyStudiesQuery();

  const sibebarClass = classNames(styles.headerSidebar, {
    [styles.visible]: isVisible,
    [styles.hidden]: !isVisible,
  });

  return (
    <div
      className={styles.headerSidebarContainer}
      onMouseEnter={showSidebar}
      onMouseLeave={hideSidebar}
    >
      <div className={sibebarClass}>
        <div className={styles.itemWrapper}>
          <div className={styles.sidebarItem}>내 스터디</div>
          {data &&
            data.joinedStudies &&
            data.joinedStudies.map((study, index) => (
              <NavLink to={`study/${study.studyId}`} key={index}>
                <div className={styles.sidebarItem}>{study.studyName}</div>
              </NavLink>
            ))}
          <NavLink to={'community'}>
            <div className={styles.sidebarItem}>금주의 스터디</div>
          </NavLink>
          <div className={styles.sidebarItem}>문제 추천</div>
          <div className={styles.sidebarItem}>전체 문제리스트</div>
        </div>
      </div>
    </div>
  );
}
