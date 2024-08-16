import { useState, useEffect } from 'react';
import styles from './TotalProblemList.module.scss';
import SearchSVG from '@/assets/icons/search.svg?react';
import { ProblemList } from './ProblemList';
import {
  useGetFilteredProblemsQuery,
  useGetProblemsQuery,
} from '@/queries/problems';
import { useParams } from 'react-router-dom';
import { studyProblemsDetails } from '@/types/problems';

export function TotalProblemList() {
  const [selectedOption, setSelectedOption] = useState<number>(1);
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [problemsInfo, setProblemsInfo] = useState<studyProblemsDetails>();

  const { studyId } = useParams<{ studyId: string }>();

  const { data: totalProblems } = useGetProblemsQuery(studyId!);
  const { refetch: refetchFiltered } = useGetFilteredProblemsQuery(
    studyId!,
    selectedOption,
    searchTerm,
  );

  const handleButtonClick = (option: number) => {
    setSelectedOption(option);
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const handleSearch = async () => {
    if (searchTerm) {
      const result = await refetchFiltered();
      if (result.data) {
        setProblemsInfo(result.data);
      }
    } else {
      setProblemsInfo(totalProblems);
    }
  };

  useEffect(() => {
    if (totalProblems) {
      setProblemsInfo(totalProblems);
    }
  }, [totalProblems]);

  return (
    <div className={styles.main}>
      <div className={styles.header}>
        <div className={styles.buttons}>
          <button
            className={selectedOption === 1 ? styles.selected : ''}
            onClick={() => handleButtonClick(1)}
          >
            유형
          </button>
          <button
            className={selectedOption === 3 ? styles.selected : ''}
            onClick={() => handleButtonClick(3)}
          >
            담당자
          </button>
          <button
            className={selectedOption === 4 ? styles.selected : ''}
            onClick={() => handleButtonClick(4)}
          >
            제목
          </button>
        </div>
        <div className={styles.search}>
          <button className={styles.searchButton} onClick={handleSearch}>
            <SearchSVG />
          </button>
          <input
            type="text"
            placeholder="검색"
            value={searchTerm}
            onChange={handleSearchChange}
          />
        </div>
      </div>
      <ProblemList problemsInfo={problemsInfo} />
    </div>
  );
}
