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
