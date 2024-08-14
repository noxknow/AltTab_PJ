export type studyProblemForm = {
  studyId: number;
  deadline: string;
  presenter: string;
  problemId: number;
};

export type studyProblemDetails = {
  studyId: number;
  deadline: string;
  studyProblems: studyProblemDetail[];
};

export type studyProblemDetail = {
  studyProblemId: number;
  problemId: number;
  title: string;
  tag: string;
  level: number;
  presenter: string;
  members: string[];
  size: number;
};

export type deleteProblemForm = {
  studyId: number;
  deadline: string;
  problemId: number;
};

export type schedules = {
  deadlines: string[];
};
