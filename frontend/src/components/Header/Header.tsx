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
    <>
      <div className={styles.header}>
        <HamburgerSVG width={30} height={30} />
        <NavLink to="/">
          <LogoSVG width={148} height={70} />
        </NavLink>
        {isLoggedIn ? (
          <div className={styles.profile}>
            <Alarm />
            <UserSVG width={40} height={40} />
          </div>
        ) : (
          <Button
            color="black"
            fill={false}
            size="small"
            onClick={onClickLogin}
          >
            <GithubSVG />
            시작하기
          </Button>
        )}
      </div>
    </>
  );
}
