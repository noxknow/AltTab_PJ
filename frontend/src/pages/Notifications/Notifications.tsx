import { Notice } from './Notice';
import styles from './Notifications.module.scss';
import AlarmSVG from '@/assets/icons/alarm.svg?react';

export function Notifications() {
  const notices = [
    {
      id: 1,
      content: '회의 일정이 변경되었습니다.',
      date: '2024-08-11 09:30:00',
    },
    {
      id: 2,
      content: '새로운 메시지가 도착했습니다.',
      date: '2024-08-11 10:15:00',
    },
    {
      id: 3,
      content: '비밀번호 변경이 필요합니다.',
      date: '2024-08-11 11:45:00',
    },
  ];

  return (
    <div className={styles.main}>
      <div className={styles.header}>
        <AlarmSVG width={40} height={40} fill="#FFBF00" />
        <div>알림</div>
      </div>
      <div>
        {notices &&
          notices.map((notice) => (
            <Notice
              key={notice.id}
              content={notice.content}
              date={notice.date}
            />
          ))}
      </div>
    </div>
  );
}
