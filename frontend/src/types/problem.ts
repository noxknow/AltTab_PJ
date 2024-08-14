export type ProblemDetailsResponse = {
  problem_id: string;
  description: string;
  input_description: string;
  output_description: string;
  sample_input: string;
  sample_output: string;
};

export type SearchProblemResponse = {
  problem_id: string;
  problem_title: string;
};

export type SearchProblemListResponse = {
  problems: SearchProblemResponse[];
};