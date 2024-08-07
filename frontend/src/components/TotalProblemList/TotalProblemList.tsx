import { useState } from 'react';
import ProblemList from '../ProblemList/ProblemList';
import styles from './TotalProblemList.module.scss';
import { SearchIcon } from '../SearchIcon/SearchIcon';

export function TotalProblemList() {
  const [selectedButton, setSelectedButton] = useState('');

  const handleButtonClick = (buttonName: string) => {
    setSelectedButton(buttonName);
  };

  return (
    <div className={styles.main}>
      <div className={styles.header}>
        <div className={styles.buttons}>
          <button
            className={selectedButton === '유형' ? styles.selected : ''}
            onClick={() => handleButtonClick('유형')}
          >
            유형
          </button>
          <button
            className={selectedButton === '난이도' ? styles.selected : ''}
            onClick={() => handleButtonClick('난이도')}
          >
            난이도
          </button>
          <button
            className={selectedButton === '담당자' ? styles.selected : ''}
            onClick={() => handleButtonClick('담당자')}
          >
            담당자
          </button>
        </div>
        <div className={styles.search}>
          <button className={styles.searchButton}>
            <SearchIcon />
          </button>

          <input type="text" placeholder="검색" />
        </div>
      </div>
      <ProblemList styleType="big" />
    </div>
  );
}
