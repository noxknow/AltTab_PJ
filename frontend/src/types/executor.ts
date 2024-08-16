export type requestExecutor = {
  studyId: string;
  problemId: string;
  memberId: string;
  code: string;
  input: string;
};

export type responseExecutor = {
  studyId: number;
  problemId: number;
  memberId: number;
  status: string;
  output: string | null;
  errorMessage: string | null;
};
