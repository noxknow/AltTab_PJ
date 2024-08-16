import { createContext, useCallback, useEffect, useState } from 'react';
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
  const [studyMember, setStudyMember] = useState<memberInfo[] | undefined>();
  const { data, refetch } = useGetStudyMemberQuery(studyId!);
  const { data: userInfo } = useGetMyInfoQuery();
  const [isMember, setIsMember] = useState(
    data?.some((member) => member.memberId === userInfo?.memberId),
  );

  const getStudyMembers = useCallback(async () => {
    const { data: newStudyMembers } = await refetch();
    if (!newStudyMembers) {
      setStudyMember([]);
      setIsMember(false);
      return;
    }
    setStudyMember(newStudyMembers);
    setIsMember(
      newStudyMembers.some((member) => member.memberId === userInfo?.memberId),
    );
  }, [userInfo, setStudyMember, setIsMember]);

  useEffect(() => {
    getStudyMembers();
  }, [studyId, userInfo]);

  return (
    <StudyContext.Provider value={{ studyMember, isMember }}>
      {children}
    </StudyContext.Provider>
  );
}
