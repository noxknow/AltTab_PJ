import React, { useState } from 'react';

import { Button } from '@/components/Button/Button';
import { Tool } from '@/components/Tool/Tool';
import { People } from '@/components/People/People';
import { Info } from '@/components/Info/Info';
import { Input } from '@/components/Input/Input';
import { Check } from '@/components/Check/Check';
import { studyInfo } from '@/types/study.ts';
import { study } from '@/services/study';

import styles from './NewStudy.module.scss';

export function NewStudy() {
  const [studyName, setStudyName] = useState('');
  const [studyEmails, setStudyEmails] = useState(['']);
  const [studyInfo, setStudyInfo] = useState('');

  const handleEmailChange = (index: number, value: string) => {
    const newEmails = [...studyEmails];
    newEmails[index] = value;
    setStudyEmails(newEmails);
  };

  const addEmailField = () => {
    setStudyEmails([...studyEmails, '']);
  };

  const createStudy = async () => {
    try {
      const form: studyInfo = {  
        studyName: studyName!,
        studyEmails: studyEmails.filter(email => email !== '')!,
        studyInfo: studyInfo!,
      }
      await study.create(form);
    } catch (error) {
      console.error('스터디 생성 실패:', error);
    }
  };

  return (
    <div className={styles.main}>
      <div className={styles.title}>스터디 생성</div>
      <div className={styles.main_mid}>
        <div className={styles.option}>
          <Tool />
          <div>
            <div className={styles.small_title}>팀명 생성</div>
            <Input 
              placeholder="팀명을 입력하세요" 
              maxLength={15} 
              value={studyName}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setStudyName(e.target.value)}
            />
          </div>
        </div>
        <div className={styles.option}>
          <People />
          <div>
            <div className={styles.small_title}>팀 초대</div>
            {studyEmails.map((email, index) => (
              <Input 
                key={index}
                type="email" 
                placeholder="팀원을 초대하세요" 
                value={email}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => handleEmailChange(index, e.target.value)}
              />
            ))}
            <div className={styles.addMemberButtonWrapper}>
              <Button color="green" fill={false} size="small" onClick={addEmailField}>+ 팀원 추가</Button>
            </div>
          </div>
        </div>
        <div className={styles.option}>
          <Info />
          <div>
            <div className={styles.small_title}>Info</div>
            <Input 
              placeholder="스터디를 소개해 주세요" 
              maxLength={25}
              value={studyInfo}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setStudyInfo(e.target.value)}
            />
          </div>
        </div>
      </div>
      <Button color="green" fill={true} size="long" onClick={createStudy}>
        <Check />
        <span>생성</span>
      </Button>
    </div>
  );
}