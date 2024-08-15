export type ProblemDetailsResponse = {
  problem_id: string;
  description: string;
  input_description: string;
  output_description: string;
  sample_input: string;
  sample_output: string;
};

export type SearchProblemResponse = {
  problemId: string;
  title: string;
  problemIdTitle: string;
};

export type SearchProblemListResponse = {
  problems: SearchProblemResponse[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
};