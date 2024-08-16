import { useState, useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import ToolSVG from '@/assets/icons/tool.svg?react';
import PeopleSVG from '@/assets/icons/people.svg?react';
import InfoSVG from '@/assets/icons/info.svg?react';
import CheckSVG from '@/assets/icons/check.svg?react';
import CloseSVG from '@/assets/icons/close.svg?react';
import { Button } from '@/components/Button/Button';
import { Input } from '@/components/Input/Input';
import { studyInfo, memberInfo } from '@/types/study.ts';
import { useCreateStudyQuery } from '@/queries/study';
import { useGetMembersByNameQuery } from '@/queries/member';
import { useDebounce } from '@/hooks/useDebounce';

import styles from './NewStudy.module.scss';

const MAX_MEMBER_COUNT = 5;
const DEBOUNCE_DELAY = 1000;

export function NewStudy() {
  const [searchValue, setSearchValue] = useState('');
  const [searchResult, setSearchResult] = useState<memberInfo[] | null>(null);
  const [studyName, setStudyName] = useState('');
  const [studyMembers, setStudyMembers] = useState<memberInfo[]>([]);
  const [studyDescription, setStudyDescription] = useState('');
  const navigate = useNavigate();
  const createStudyQuery = useCreateStudyQuery();
  const { refetch } = useGetMembersByNameQuery(searchValue);
  const debouncedQuery = useDebounce(searchValue, DEBOUNCE_DELAY);

  const getMembersByName = useCallback(async () => {
    if (searchValue === '') {
      setSearchResult(null);
      return;
    }
    const { data } = await refetch();
    if (data && data.members) {
      setSearchResult(data.members);
    }
  }, [searchValue]);

  useEffect(() => {
    getMembersByName();
  }, [debouncedQuery]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newMemberName = e.currentTarget.value;
    setSearchValue(newMemberName);
  };

  const addMemberField = (newMember: memberInfo) => {
    setStudyMembers((prevMembers) => {
      if (!newMember) {
        return prevMembers;
      }
      if (
        prevMembers.some((member) => member.memberId === newMember.memberId)
      ) {
        return prevMembers;
      }
      const newMembers = [...prevMembers, newMember];
      setSearchResult(null);
      setSearchValue('');
      return newMembers;
    });
  };

  const deleteMember = useCallback(
    (target: number) => {
      setStudyMembers((prevMembers) => {
        return prevMembers.filter((_, index) => index !== target);
      });
    },
    [setStudyMembers],
  );

  const createStudy = async () => {
    if (!studyName) {
      alert('스터디 명을 입력해주세요');
      return;
    }

    if (!studyDescription) {
      alert('스터디 설명을 입력해주세요');
      return;
    }

    try {
      const form: studyInfo = {
        studyName,
        memberIds: studyMembers.map((member) => member.memberId.toString())!,
        studyDescription,
      };

      const { studyId } = await createStudyQuery.mutateAsync(form);
      navigate(`/study/${studyId}`);
      window.location.reload();
    } catch (error) {
      console.error('스터디 생성 실패:', error);
    }
  };

  return (
    <div className={styles.main}>
      <div className={styles.title}>스터디 생성</div>
      <div className={styles.main_mid}>
        <div className={styles.option}>
          <ToolSVG />
          <div className={styles.content}>
            <div className={styles.small_title}>팀명</div>
            <Input
              placeholder="팀명을 입력하세요"
              maxLength={15}
              value={studyName}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                setStudyName(e.target.value)
              }
            />
          </div>
        </div>
        <div className={styles.option}>
          <PeopleSVG />
          <div className={styles.content}>
            <div className={styles.small_title}>
              팀원 초대 {studyMembers.length}/{MAX_MEMBER_COUNT}
            </div>
            <div className={styles.study_members}>
              <div className={styles.member_container}>
                {studyMembers.length < MAX_MEMBER_COUNT && (
                  <div className={styles.new_member}>
                    <Input
                      type="name"
                      value={searchValue}
                      placeholder="팀원을 초대하세요"
                      onChange={handleChange}
                      onFocus={getMembersByName}
                      onBlur={() => {
                        setSearchResult(null);
                      }}
                    />
                    {searchResult && (
                      <div className={styles.search_result}>
                        {searchResult.map((member) => (
                          <Button
                            key={member.memberId}
                            color="green"
                            fill={false}
                            size="long"
                            onMouseDown={() => addMemberField(member)}
                          >
                            {member.name}
                          </Button>
                        ))}
                      </div>
                    )}
                  </div>
                )}
                <div className={styles.members}>
                  {studyMembers.map((member, index) => (
                    <div key={index} className={styles.member}>
                      <Input type="name" value={member.name} readOnly />
                      <Button
                        className={styles.close_button}
                        color="red"
                        fill={false}
                        size="small"
                        onClick={() => deleteMember(index)}
                      >
                        <CloseSVG width={24} height={24} stroke="#F24242" />
                      </Button>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className={styles.option}>
          <InfoSVG />
          <div className={styles.content}>
            <div className={styles.small_title}>스터디 소개</div>
            <Input
              placeholder="스터디를 소개해 주세요"
              maxLength={25}
              value={studyDescription}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                setStudyDescription(e.target.value)
              }
            />
          </div>
        </div>
      </div>
      <Button color="green" fill={true} size="long" onClick={createStudy}>
        <CheckSVG />
        <span>생성</span>
      </Button>
    </div>
  );
}
