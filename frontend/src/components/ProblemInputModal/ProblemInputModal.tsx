import React, { useState } from 'react';
import { Button } from '@/components/Button/Button';
import { SearchIcon } from '@/components/SearchIcon/SearchIcon';
import { PencilIcon } from '@/components/PencilIcon/PencilIcon';
import CloseSVG from '@/assets/icons/close.svg?react';
import styles from './ProblemInputModal.module.scss';

type ProblemInputModalProps = {
  open: boolean;
  onClose: () => void;
};

export default function ProblemInputModal({
  open,
  onClose,
}: ProblemInputModalProps) {
  const [keyword, setKeyword] = useState('');
  const [results, setResults] = useState<string[]>([]);
  const [selectedResult, setSelectedResult] = useState<string | null>(null);

  const handleKeywordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newKeyword = event.target.value;
    setKeyword(newKeyword);

    const dummyResults = newKeyword
      ? Array.from(
          { length: 5 },
          (_, index) => `${newKeyword} Result ${index + 1}`,
        )
      : [];

    setResults(dummyResults);
  };

  const handleResultClick = (result: string) => {
    setSelectedResult(result);
  };

  const handleRegisterClick = () => {
    if (selectedResult) {
      console.log('Selected Result:', selectedResult);
    } else {
      console.log('No result selected');
    }
  };

  if (!open) return null;

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
              <SearchIcon />
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
                  <PencilIcon />
                  {result}
                </div>
              ))
            ) : (
              <div className={styles.noResult}>검색 결과가 없습니다.</div>
            )}
          </div>
          <Button
            color="green"
            fill={true}
            size="long"
            onClick={handleRegisterClick}
          >
            등록하기
          </Button>
        </div>
      </div>
    </div>
  );
}
