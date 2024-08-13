import { Notice } from './Notice';
import styles from './Notifications.module.scss';

import AlarmSVG from '@/assets/icons/alarm.svg?react';
import { useNotificationQuery, useCheckNotificationMutation } from '@/queries/notice';

export function Notifications() {
  const { data: notifications, isLoading, error } = useNotificationQuery();
  const checkNotificationMutation = useCheckNotificationMutation();

  const handleCheck = (notificationId: number, studyId: number, check: boolean) => {
    checkNotificationMutation.mutate({
      notificationId,
      studyId,
      check,
    });
  };

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error loading notifications</div>;

  return (
    <div className={styles.main}>
      <div className={styles.header}>
        <AlarmSVG width={40} height={40} fill="#FFBF00" />
        <div>알림</div>
      </div>
      <div>
        {notifications && notifications.notifications.map((notice) => (
          <Notice
            key={notice.notificationId}
            content={`${notice.studyName}에 초대 되었습니다.`}
            date={notice.createdAt}
            onAccept={() => handleCheck(notice.notificationId, notice.studyId, true)}
            onReject={() => handleCheck(notice.notificationId, notice.studyId, false)}
          />
        ))}
      </div>
    </div>
  );
}