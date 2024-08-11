import { NavLink } from 'react-router-dom';

import AlarmSVG from '@/assets/icons/alarm.svg?react';
import { MemberProfile } from '@/components/StudyLeftBar/MemberProfile';
import { Button } from '@/components/Button/Button';
import { useLogoutQuery } from '@/queries/member';
import { URL } from '@/constants/url';

import styles from './ProfileModal.module.scss';

type ProfileModalProps = {
  setIsModal: (isOpen: boolean) => void;
  url: string;
  name: string;
};

export function ProfileModal({ setIsModal, url, name }: ProfileModalProps) {
  const { mutateAsync: logout } = useLogoutQuery();

  const handleLogout = async () => {
    await logout();
    window.location.href = `${URL.MAIN}`;
  };

  return (
    <>
      <div
        className={styles.overlay}
        onMouseEnter={() => setIsModal(true)}
        onMouseLeave={() => setIsModal(false)}
      >
        <div className={styles.modal}>
          <div className={styles.content}>
            <div className={styles.header}>내 정보</div>
            <MemberProfile url={url} name={name} point={100} />
            <div className={styles.notice}>
              <NavLink to="/notifications" className={styles.notifications}>
                <div className={styles.alarm}>
                  <AlarmSVG />
                  <div>알림</div>
                </div>
                <div className={styles.number}>
                  <div>7</div>
                </div>
              </NavLink>
            </div>
            <Button
              className={styles.logout}
              color="black"
              fill={false}
              size="small"
              onClick={handleLogout}
            >
              로그아웃
            </Button>
          </div>
        </div>
      </div>
    </>
  );
}
