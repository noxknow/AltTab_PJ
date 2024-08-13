import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { useMutation } from '@tanstack/react-query';
import { recommend } from '@/services/recommend';
import { RecommendationResponse } from '@/types/recommend';
import { Loading } from '@/components/Loading/Loading';

import { Section } from './Section';
import styles from './Recommend.module.scss';

export function Recommend() {
  const { studyId } = useParams<{ studyId: string }>();

  const [recommendations, setRecommendations] =
    useState<RecommendationResponse>({
      collaborative: [],
      least_common_representative: [],
      most_common_representative: [],
    });

  const mutation = useMutation({
    mutationFn: () =>
      recommend.getRecommendations({ study_id: Number(studyId) }),

    onSuccess: (data) => {
      setRecommendations(data);
    },

    onError: (error) => {
      console.error('추천 데이터를 가져오는 중 오류가 발생했습니다:', error);
    },
  });

  useEffect(() => {
    mutation.mutate(); // POST 요청 실행
  }, [studyId]);

  // if (mutation.isPending) return <div>Loading...</div>;
  if (mutation.isPending) {
    return <Loading />;
  }
  if (mutation.isError)
    return <div>Error loading recommendations: {mutation.error?.message}</div>;

  return (
    <div className={styles.container}>
      <Section title="AI 추천 문제" problems={recommendations.collaborative} />
      <Section
        title="약점 보완"
        problems={recommendations.least_common_representative}
      />
      <Section
        title="강점 강화"
        problems={recommendations.most_common_representative}
      />
    </div>
  );
}
