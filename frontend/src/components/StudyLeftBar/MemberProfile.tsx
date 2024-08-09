import styles from './MemberProfile.module.scss';

type MemberProfileProps = {
  url: string;
  name: string;
  point?: number;
};

export function MemberProfile({ url, name, point = 0 }: MemberProfileProps) {
  return (
    <div className={styles.main}>
      <div>
        <img className={styles.profile_image} src={url} alt="No Image" />
      </div>
      <div className={styles.right}>
        <div className={styles.name}>{name}</div>
        <div className={styles.point}>
          {point.toLocaleString('ko-KR')}
          <span className={styles.p}> P</span>
        </div>
      </div>
    </div>
  );
}
