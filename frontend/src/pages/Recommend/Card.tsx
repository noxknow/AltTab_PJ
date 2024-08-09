import styles from './Card.module.scss'; 

interface CardProps {
  title: string;
  tags: string;
  level: number; // 레벨 정보
  problemId: number; // 문제 ID
}

export function Card({ title, tags, level, problemId }: CardProps) {
  const handleClick = () => { 
    window.open(`https://www.acmicpc.net/problem/${problemId}`, '_blank');
  };

  // 레벨에 따른 클래스 설정
  const levelToClassName = (level: number) => {
    if (level >= 1 && level <= 5) {
      return styles.bronze;
    } else if (level >= 6 && level <= 10) {
      return styles.silver;
    } else if (level >= 11 && level <= 15) {
      return styles.gold;
    } else if (level >= 16 && level <= 20) {
      return styles.platinum;
    } else if (level >= 21 && level <= 25) {
      return styles.diamond;
    } else if (level >= 26 && level <= 30) {
      return styles.ruby;
    }
  };

  // 레벨에 따른 배지 이미지 설정
  const levelToTierImage = (level: number) => {
    return `https://d2gd6pc034wcta.cloudfront.net/tier/${level}.svg`;
  };

  return (
    <div className={`${styles.card} ${levelToClassName(level)}`}>
      <div className={styles.content}>
        <div className={styles.cardHeader}>
          <span>{title}</span>
        </div>
        <div className={styles.cardBody}>
          <span>{tags}</span>
        </div>
        <button className={styles.cardButton} onClick={handleClick}>
          문제 보러가기
        </button>
      </div>
      <img src={levelToTierImage(level)} width="40" alt={`Tier ${level}`} />
    </div>
  );
}
