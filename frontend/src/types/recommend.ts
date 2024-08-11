// recommend.ts
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
  collaborative: Problem[];
  least_common_representative: Problem[];
  most_common_representative: Problem[];
};

export interface SectionProps {
  title: string;
  problems: Problem[];
}
