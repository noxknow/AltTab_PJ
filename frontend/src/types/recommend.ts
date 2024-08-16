export type Problem = {
  problem_id: number;
  title: string;
  representative: string;
  tag: string;
  level: number;
};

export type RecommendationRequest = {
  study_id: number;
};

export type RecommendationResponse = {
  recommendations: Problem[];
  least_common_representative_opposite: Problem[];
  most_common_representative: Problem[];
};

export type SectionProps = {
  title: string;
  problems: Problem[];
};
