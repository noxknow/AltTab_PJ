export type memberInfo = {
    memberId?: number;
    username: string;
    memberName: string;
    memberEmail?: string;
    memberAvatarUrl: string;
    memberHtmlUrl: string; 
    role: string; 
}

export type studyInfo = {
    studyName?: string;
    studyEmails?: string[];
    studyDescription?: string;
};

export type studyMemberInfo = {
    members?: memberInfo[];
    studies?: studyInfo[];
};
  