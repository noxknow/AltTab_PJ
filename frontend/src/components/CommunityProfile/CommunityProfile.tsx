import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import styles from '@/components/CommunityProfile/CommunityProfile.module.scss';
import FullHeartSVG from '@/assets/icons/full_heart.svg?react';
import EmptyHeartSVG from '@/assets/icons/empty_heart.svg?react';
import { useFollowQuery } from '@/queries/community';
import { communityStudy } from '@/types/study';

export function CommunityProfile({
  study,
  isLink = true,
}: {
  study: communityStudy;
  isLink: boolean;
}) {
  const [likeCount, setLikeCount] = useState<number>();
  const [check, setCheck] = useState<boolean>();
  const follow = useFollowQuery();
  const navigate = useNavigate();

  useEffect(() => {
    setLikeCount(study.like);
    setCheck(study.check);
  }, [study]);

  const onClickFollow = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    const data = await follow.mutateAsync(study.studyId.toString());
    setLikeCount(data.like);
    setCheck(data.check);
  };

  const handleClick = () => {
    if (isLink) {
      navigate(`/study/${study.studyId}`);
    }
  };

  return (
    <div onClick={handleClick} className={styles.profile_card}>
      <div className={styles.profile_card_title}>
        <div className={styles.profile_study}>{study.studyName}</div>
        <button onClick={onClickFollow} className={styles.like}>
          {check ? <FullHeartSVG /> : <EmptyHeartSVG />}
          <div className={styles.like_number}>{likeCount}</div>
        </button>
      </div>
      <div className={styles.profile_card_body}>
        <img
          className={styles.profile_image}
          src={study.leaderMemberDto.avatarUrl}
          alt="Study Leader"
        />
        <div className={styles.description}>
          <div className={styles.profile_name}>
            {study.leaderMemberDto.name}
          </div>
          <div className={styles.profile_detail}>{study.studyDescription}</div>
        </div>
      </div>
    </div>
  );
}
