import { MemberProfile } from '@/components/StudyLeftBar/MemberProfile';
import styles from './ProfileModal.module.scss';
import AlarmSVG from '@/assets/icons/alarm.svg?react';
import { Button } from '../Button/Button';
import { NavLink } from 'react-router-dom';

type ProfileModalProps = {
  open: boolean;
  setIsModal: (isOpen: boolean) => void;
  url: string;
  name: string;
};

export function ProfileModal({
  open,
  setIsModal,
  url,
  name,
}: ProfileModalProps) {
  if (!open) return null;

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
              <div className={styles.alarm}>
                <AlarmSVG />
                <div>알림</div>
              </div>
              <NavLink to="/notifications">
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
            >
              로그아웃
            </Button>
          </div>
        </div>
      </div>
    </>
  );
}
