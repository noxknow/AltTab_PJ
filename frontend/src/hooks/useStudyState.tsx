import { useContext } from 'react';
import { StudyContext } from '@/contexts/study';

export function useStudyState() {
  const context = useContext(StudyContext);
  if (context === undefined) {
    throw new Error('useStudyState should be used within StudyContext');
  }
  const { studyMember, isMember } = context;
  return { studyMember, isMember };
}
