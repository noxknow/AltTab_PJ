import { NavLink } from 'react-router-dom';

import LogoSVG from '@/assets/icons/logo.svg?react';
import HamburgerSVG from '@/assets/icons/hamburger.svg?react';
import GithubSVG from '@/assets/icons/github.svg?react';
import UserSVG from '@/assets/icons/user.svg?react';
import { Button } from '@/components/Button/Button';
import { Alarm } from '@/components/Alarm/Alarm';
import { URL } from '@/constants/url';

import styles from './Header.module.scss';

export function Header() {
  // TODO : 사용자 로그인 상태 처리
  const isLoggedIn: boolean = false;

  return (
    <header className={styles.header}>
      <div className={`${styles.header_item} ${styles.left_item}`}>
        <button className={styles.button}>
          <HamburgerSVG width={30} height={30} />
        </button>
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
          <NavLink to={`${URL.LOGIN}/oauth2/authorization/github`}>
            <Button color="black" fill={false} size="small">
              <GithubSVG />
              <div className={styles.login}>시작하기</div>
            </Button>
          </NavLink>
        </div>
      )}
    </header>
  );
}
