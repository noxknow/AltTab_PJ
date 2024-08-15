export type studyProblemsDetails = {
  problemList: studyProblemsDetail[];
};

export type studyProblemsDetail = {
  studyProblemId: number;
  problemId: number;
  title: string;
  tag: string;
  level: number;
  presenter: string;
  deadline: string;
};
