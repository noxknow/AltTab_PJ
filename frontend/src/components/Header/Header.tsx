import { NavLink } from 'react-router-dom';

import LogoSVG from '@/assets/icons/logo.svg?react';
import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import GithubSVG from '@/assets/icons/github.svg?react';
import UserSVG from '@/assets/icons/user.svg?react';
import { Button } from '@/components/Button/Button';
import { Alarm } from '@/components/Alarm/Alarm';

import styles from './Header.module.scss';

export function Header() {
  // TODO : 사용자 로그인 상태 처리
  const isLoggedIn: boolean = false;

  // TODO :
  function onClickLogin() {
    console.log('login');
  }

  return (
    <div className={styles.header}>
      <div className={`${styles.header_item} ${styles.left_item}`}>
        <HamburgerSVG width={30} height={30} />
      </div>

      <div className={`${styles.header_item} ${styles.middle_item}`}>
        <NavLink to="/">
          <LogoSVG width={148} height={70} />
        </NavLink>
      </div>

      {isLoggedIn ? (
        <div className={`${styles.profile} ${styles.header_item}`}>
          <Alarm />
          <UserSVG width={40} height={40} />
        </div>
      ) : (
        <div className={`${styles.header_item} ${styles.right_item}`}>
          <Button
            color="black"
            fill={false}
            size="small"
            onClick={onClickLogin}
          >
            <GithubSVG />
            시작하기
          </Button>
        </div>
      )}
    </div>
  );
}
