export type requestExecutor = {
  studyGroupId: string;
  problemId: string;
  problemTab: string;
  code: string;
  input: string;
};

export type responseExecutor = {
  studyGroupId: number;
  problemId: number;
  problemTab: number;
  status: string;
  output: string | null;
  errorMessage: string | null;
};
