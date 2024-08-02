import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

import { studyInfo } from '@/types/study.ts';
import { study } from '@/services/study';

import StudyIntro from './StudyIntro';
import StudyMembers from './StudyMembers';
import StudySchedule from './StudySchedule';

import styles from './StudyLeftBar.module.scss';

export default function StudyLeftBar() {
  const { studyId } = useParams<{ studyId: string }>();
  const [studyInfo, setStudyInfo] = useState<studyInfo>({});

  const loadStudyInfo = async () => {
    if (!studyId) return;

    try {
      const data = await study.loadInfo(studyId);
      setStudyInfo(data);
    } catch (error) {
      console.error('스터디 정보 로드 실패:', error);
    }
  }

  useEffect(() => {
    loadStudyInfo();
  }, [studyId]);

  return (
    <div className={styles.main}>
      <StudyIntro 
        studyName={studyInfo.studyName} 
        studyDescription={studyInfo.studyDescription} 
      />
      <StudySchedule date={new Date('2024-08-05 20:00:00')} />
      <StudyMembers />
    </div>
  );
}
