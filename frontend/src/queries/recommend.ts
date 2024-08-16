import { useMutation, QueryClient } from '@tanstack/react-query';
import { recommend } from '@/services/recommend';

const recommendKeys = {
  recommendations: ['recommendations'],
};

export const useGetRecommendationsQuery = (studyId: number) => {
  const queryClient = new QueryClient();

  return useMutation({
    mutationFn: () => recommend.getRecommendations({ study_id: studyId }),

    // 요청이 성공했을 때 호출
    onSuccess: (data) => {
      console.log('추천 데이터를 성공적으로 가져왔습니다:', data); // 데이터 확인
      queryClient.invalidateQueries({
        queryKey: [...recommendKeys.recommendations, studyId],
      });
    },

    // 요청이 실패했을 때 호출
    onError: (error) => {
      console.error('추천 데이터를 가져오는 중 오류가 발생했습니다:', error); // 오류 확인
    },

    // 요청이 완료되었을 때(성공, 실패 상관없이) 호출
    onSettled: () => {
      console.log('추천 데이터 가져오기 요청이 완료되었습니다.'); // 완료 확인
    },
  });
};
