export type memberInfo = {
  memberId: number;
  name: string;
  avatarUrl: string;
  point?: number;
};

export type studyInfo = {
  studyId?: string;
  studyName?: string;
  studyEmails?: string[];
  studyDescription?: string;
};

export type joinedStudies = {
  joinedStudies: studyInfo[];
};

export type communityStudy = {
  studyId: number;
  name: string;
  like: number;
  follower: number;
  view: number;
};

export type weeklyStudies = {
  weeklyStudies: communityStudy[];
  topSolvers: communityStudy[];
};
