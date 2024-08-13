export type memberInfo = {
  memberId: number;
  name: string;
  avatarUrl: string;
  point?: number;
};

export type studyInfo = {
  studyId?: string;
  studyName?: string;
  memberIds?: string[];
  studyDescription?: string;
};

export type searchedMembers = {
  members: memberInfo[];
};

export type joinedStudies = {
  joinedStudies: studyInfo[];
};

export type communityStudy = {
  studyId: number;
  name: string;
  studyDescription: string;
  like: number;
  follower?: number;
  totalSolve?: number;
  totalFollower?: number;
  view: number;
  leaderMemberDto: {
    name: string;
    avatarUrl: string;
  };
};

export type weeklyStudies = {
  weeklyStudies: communityStudy[];
  topSolvers: communityStudy[];
};
