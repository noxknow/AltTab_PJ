import { useState } from 'react';
import { Button } from '@/components/Button/Button';
import SearchSVG from '@/assets/icons/search.svg?react';
import PencilSVG from '@/assets/icons/pencil.svg?react';
import CloseSVG from '@/assets/icons/close.svg?react';
import styles from './ProblemInputModal.module.scss';
import { usePostProblemQuery } from '@/queries/schedule';
import { useClickedDate } from '@/hooks/useClickedDate';
import { useParams } from 'react-router-dom';
import { useGetMyInfoQuery } from '@/queries/member';

type ProblemInputModalProps = {
  open: boolean;
  onClose: () => void;
  refetchSchedule: () => Promise<void>;
};

export function ProblemInputModal({
  open,
  onClose,
  refetchSchedule,
}: ProblemInputModalProps) {
  if (!open) return null;

  const [keyword, setKeyword] = useState('');
  const [results, setResults] = useState<number[]>([]);
  const [selectedResult, setSelectedResult] = useState<number>(0);
  const { clickedDate } = useClickedDate();
  const postProblemMutation = usePostProblemQuery();
  const { data: userInfo } = useGetMyInfoQuery();
  const { studyId } = useParams<{ studyId: string }>();

  const handleKeywordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newKeyword = event.target.value;
    setKeyword(newKeyword);

    const dummyResults = Array.from({ length: 10 }, (_, index) => 1000 + index);

    setResults(dummyResults);
  };

  const handleResultClick = (result: number) => {
    setSelectedResult(result);
  };

  const handleRegisterClick = async () => {
    if (selectedResult && studyId) {
      const form = {
        studyId: parseInt(studyId, 10),
        deadline: clickedDate,
        presenter: userInfo?.name || '',
        problemId: selectedResult,
      };
      await postProblemMutation.mutateAsync(form);
      refetchSchedule();
    }
  };

  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <div className={styles.content}>
          <div className={styles.header}>문제 등록</div>

          <div className={styles.input}>
            <input
              type="text"
              value={keyword}
              onChange={handleKeywordChange}
              placeholder="검색어 입력"
            />
            <button className={styles.searchButton}>
              <SearchSVG />
            </button>
          </div>

          <button className={styles.closeButton} onClick={onClose}>
            <CloseSVG width={24} height={24} stroke="#F24242" />
          </button>

          <div className={styles.result}>
            {results.length > 0 ? (
              results.map((result, index) => (
                <div
                  key={index}
                  className={`${styles.resultItem} ${selectedResult === result ? styles.selected : ''}`}
                  onClick={() => handleResultClick(result)}
                >
                  <PencilSVG />
                  {result}
                </div>
              ))
            ) : (
              <div className={styles.noResult}>검색 결과가 없습니다.</div>
            )}
          </div>
          <Button
            className={styles.register}
            color="green"
            fill={true}
            size="small"
            onClick={handleRegisterClick}
          >
            등록하기
          </Button>
        </div>
      </div>
    </div>
  );
}
