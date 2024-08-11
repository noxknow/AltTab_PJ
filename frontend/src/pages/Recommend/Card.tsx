import styles from './Card.module.scss';
import { Problem } from '@/types/recommend';

export function Card({
  title,
  representative,
  tag,
  level,
  problem_id,
}: Problem) {
  const handleClick = () => {
    window.open(`https://www.acmicpc.net/problem/${problem_id}`, '_blank');
  };

  const levelToTierImage = (level: number) => {
    return `https://d2gd6pc034wcta.cloudfront.net/tier/${level}.svg`; // 티어 이미지 URL
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

  // 태그 파싱하고 최대 3개까지만 표시
  const parsedTags = tag.split(';').slice(0, 3);

  return (
    <div className={`${styles.card} ${levelClass}`}>
      <div className={styles.content}>
        <div className={styles.cardHeader}>
          <span>{title}</span>
        </div>
        <div className={styles.cardBody}>
          <span>{representative}</span>
        </div>
        <div className={styles.tags}>
          {parsedTags.map((t, index) => (
            <span key={index} className={styles.tagItem}>
              {t.trim()}
            </span>
          ))}
        </div>
        <button className={styles.cardButton} onClick={handleClick}>
          문제 보러가기
        </button>
      </div>
      <img
        src={levelToTierImage(level)}
        className={styles.tierImage}
        alt={`Tier ${level}`}
      />
    </div>
  );
}
