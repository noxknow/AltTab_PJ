import { useState } from 'react';
import styles from './Card.module.scss';
import { Problem } from '@/types/recommend';

export function Card({ title, tag, level, problem_id }: Problem) {
  const [flipped, setFlipped] = useState(false);

  const handleFlip = () => {
    setFlipped(!flipped);
  };

  const handleClick = () => {
    window.open(`https://www.acmicpc.net/problem/${problem_id}`, '_blank');
  };

  const levelToTierImage = (level: number) => {
    return `https://d2gd6pc034wcta.cloudfront.net/tier/${level}.svg`;
  };

  const getLevelClass = (level: number): string => {
    if (level >= 1 && level <= 5) return styles.bronze;
    if (level >= 6 && level <= 10) return styles.silver;
    if (level >= 11 && level <= 15) return styles.gold;
    if (level >= 16 && level <= 20) return styles.platinum;
    if (level >= 21 && level <= 25) return styles.diamond;
    return styles.ruby;
  };

  const levelClass: string = getLevelClass(level);
  const parsedTags = tag.split(';');

  return (
    <div
      className={`${styles.card} ${levelClass} ${flipped ? styles.flipped : ''}`}
      onClick={handleFlip}
    >
      <div className={styles.cardInner}>
        <div className={styles.cardFront}>
          <div className={styles.cardHeader}>{title}</div>
          <button className={styles.cardButton} onClick={handleClick}>
            문제 보러가기
          </button>
        </div>
        <div className={styles.cardBack}>
          <img
            src={levelToTierImage(level)}
            className={styles.tierImage}
            alt={`Tier ${level}`}
          />
          <div className={styles.tags}>
            {parsedTags.map((t, index) => (
              <span key={index} className={styles.tagItem}>
                {t.trim()}
              </span>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
