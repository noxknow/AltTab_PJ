import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { NotificationRequestDto } from '@/types/notice';
import { notice } from '@/services/notice';

const noticeKeys = {
  count: ['count'],
  list: ['notifications'],
  check: ['notifications'],
};

export const useCountNotificationQuery = () => {
  const { data } = useQuery({
    queryKey: noticeKeys.count,
    queryFn: () => notice.countNotification(),
  });

  return { data };
};

export const useNotificationQuery = () => {
  const { data, isLoading, error } = useQuery({
    queryKey: noticeKeys.list,
    queryFn: () => notice.getNotification(),
  });

  return { data, isLoading, error };
};

export const useCheckNotificationMutation = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (requestDto : NotificationRequestDto) => notice.checkNotification(requestDto),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: noticeKeys.check,
      });
    },
  });
};