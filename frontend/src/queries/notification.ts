import { useQuery } from '@tanstack/react-query';

import { notice } from '@/services/notice';

const noticeKeys = {
  count: ['count'],
};

export const useCountNotificationQuery = () => {
  const { data } = useQuery({
    queryKey: noticeKeys.count,
    queryFn: () => notice.countNotification(),
  });

  return { data };
};