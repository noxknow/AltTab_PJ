import { createContext } from 'react';
import { useParams } from 'react-router-dom';

import { useGetStudyMemberQuery } from '@/queries/study';
import { useGetMyInfoQuery } from '@/queries/member';
import { memberInfo } from '@/types/study';

interface StudyProps {
  studyMember: memberInfo[] | undefined;
  isMember: boolean | undefined;
}

export const StudyContext = createContext<StudyProps | undefined>(undefined);

type StudyProviderProps = {
  children: React.ReactNode;
};

export function StudyProvider({ children }: StudyProviderProps) {
  const { studyId } = useParams();
  const { data: studyMember } = useGetStudyMemberQuery(studyId!);
  const { data: userInfo } = useGetMyInfoQuery();
  const isMember = studyMember?.some(
    (member) => member.memberId === userInfo?.memberId,
  );

  return (
    <StudyContext.Provider value={{ studyMember, isMember }}>
      {children}
    </StudyContext.Provider>
  );
}
