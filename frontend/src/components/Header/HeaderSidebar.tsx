import classNames from 'classnames';

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
          <div className={styles.sidebarItem}>금주의 스터디</div>
          <div className={styles.sidebarItem}>문제 추천</div>
          <div className={styles.sidebarItem}>전체 문제리스트</div>
        </div>
      </div>
    </div>
  );
}
