import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';

import { Button } from '@/components/Button/Button';
import SearchSVG from '@/assets/icons/search.svg?react';
import PencilSVG from '@/assets/icons/pencil.svg?react';
import CloseSVG from '@/assets/icons/close.svg?react';
import { usePostProblemQuery } from '@/queries/schedule';
import { useClickedDate } from '@/hooks/useClickedDate';
import { useGetMyInfoQuery } from '@/queries/member';
import { useSearchProblemsQuery } from '@/queries/searchProblem';
import { useDebounce } from '@/hooks/useDebounce';
import { SearchProblemResponse } from '@/types/problem';

import styles from './ProblemInputModal.module.scss';

type ProblemInputModalProps = {
  open: boolean;
  onClose: () => void;
  refetchSchedule: () => Promise<void>;
};

const DEBOUNCE_DELAY = 1000;
const ITEMS_PER_PAGE = 15;

export function ProblemInputModal({
  open,
  onClose,
  refetchSchedule,
}: ProblemInputModalProps) {
  if (!open) return null;

  const [keyword, setKeyword] = useState('');
  const [searchResults, setSearchResults] = useState<SearchProblemResponse[] | null>(null);
  const [selectedResult, setSelectedResult] = useState<string>('');
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const { studyId } = useParams<{ studyId: string }>();

  const { clickedDate } = useClickedDate();
  const postProblemMutation = usePostProblemQuery();
  const { data: userInfo } = useGetMyInfoQuery();
  const { isLoading, refetch } = useSearchProblemsQuery(keyword, currentPage, ITEMS_PER_PAGE);
  const debouncedQuery = useDebounce(keyword, DEBOUNCE_DELAY);

  const getKeyWordsById = useCallback(async () => {
    if (keyword === '') {
      setSearchResults(null);
      return;
    }
    const { data } = await refetch();
    if (data && data.problems) {
      setSearchResults(data.problems);
      setTotalPages(data.totalPages);
    }
  }, [keyword, currentPage]);

  useEffect(() => {
    getKeyWordsById();
  }, [debouncedQuery, currentPage]);

  const handleKeywordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newKeyword = event.target.value;
    setKeyword(newKeyword);
    setCurrentPage(0);
  };

  const handleResultClick = (result: string) => {
    setSelectedResult(result);
  };

  const handleRegisterClick = async () => {
    if (selectedResult && studyId) {
      const form = {
        studyId: parseInt(studyId, 10),
        deadline: clickedDate,
        presenter: userInfo?.name || '',
        problemId: parseInt(selectedResult, 10),
      };
      await postProblemMutation.mutateAsync(form);
      refetchSchedule();
      onClose();
    }
  };

  const handlePageChange = (newPage: number) => {
    setCurrentPage(newPage);
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
            {isLoading ? (
              <div>검색 중...</div>
            ) : searchResults && searchResults.length > 0 ? (
              <>
                {searchResults.map((result) => (
                  <div
                    key={result.problemId}
                    className={`${styles.resultItem} ${selectedResult === result.problemId ? styles.selected : ''}`}
                    onClick={() => handleResultClick(result.problemId)}
                  >
                    <PencilSVG />
                    {result.problemIdTitle}
                  </div>
                ))}
                <div className={styles.pagination}>
                  {Array.from({ length: Math.min(10, totalPages) }, (_, index) => {
                    const pageNumber = currentPage - 4 + index + 1;
                    if (pageNumber > 0 && pageNumber <= totalPages) {
                      return (
                        <button
                          key={pageNumber}
                          onClick={() => handlePageChange(pageNumber)}
                          className={currentPage + 1 === pageNumber ? styles.activePage : ''}
                        >
                          {pageNumber}
                        </button>
                      );
                    }
                    return null;
                  })}
                </div>
              </>
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
            disabled={!selectedResult}
          >
            등록하기
          </Button>
        </div>
      </div>
    </div>
  );
}