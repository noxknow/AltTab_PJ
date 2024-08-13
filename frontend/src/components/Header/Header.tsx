import { useState } from 'react';
import { NavLink } from 'react-router-dom';

import LogoSVG from '@/assets/icons/logo.svg?react';
import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import GithubSVG from '@/assets/icons/github.svg?react';
import { Button } from '@/components/Button/Button';
import { HeaderSidebar } from '@/components/Header/HeaderSidebar';
import { ProfileModal } from '@/components/ProfileModal/ProfileModal';
import { URL } from '@/constants/url';
import { useGetMyInfoQuery } from '@/queries/member';
import { useCountNotificationQuery } from '@/queries/notification';

import styles from './Header.module.scss';

export function Header() {
  const [isVisible, setIsVisible] = useState(false);
  const [isModal, setIsModal] = useState(false);
  // TODO : 사용자 로그인 상태 처리
  const { data: userInfo, isLogin } = useGetMyInfoQuery();
  const { data: notificationCount } = useCountNotificationQuery();

  const showSidebar = () => {
    setIsVisible(true);
  };

  const hideSidebar = () => {
    setIsVisible(false);
  };

  const handleLogin = () => {
    window.location.href = `${URL.LOGIN}/oauth2/authorization/github`;
  };

  return (
    <header className={styles.header}>
      <HeaderSidebar
        isVisible={isVisible}
        showSidebar={showSidebar}
        hideSidebar={hideSidebar}
      />
      <div className={`${styles.header_item} ${styles.left_item}`}>
        <button
          className={styles.button}
          onClick={() => setIsVisible((prev) => !prev)}
        >
          <HamburgerSVG width={30} height={30} />
        </button>
      </div>
      <div className={`${styles.header_item} ${styles.middle_item}`}>
        <NavLink to="/">
          <LogoSVG width={148} height={70} />
        </NavLink>
      </div>
      {isLogin && userInfo ? (
        <div className={`${styles.profile} ${styles.header_item}`}>
          <button
            className={styles.button}
            onMouseEnter={() => setIsModal(true)}
            onMouseLeave={() => setIsModal(false)}
          >
            <img
              src={userInfo!.avatarUrl}
              alt="userProfile"
              className={styles.profileImg}
            />
            {notificationCount && notificationCount > 0 && (
              <div className={styles.notificationDot}></div>
            )}
          </button>
        </div>
      ) : (
        <div className={`${styles.header_item} ${styles.right_item}`}>
          <Button color="black" fill={false} size="small" onClick={handleLogin}>
            <GithubSVG />
            <div className={styles.login}>시작하기</div>
          </Button>
        </div>
      )}
      {isModal && (
        <ProfileModal
          setIsModal={setIsModal}
          url={userInfo ? userInfo.avatarUrl : ''}
          name={userInfo ? userInfo.name : ''}
        />
      )}
    </header>
  );
}
