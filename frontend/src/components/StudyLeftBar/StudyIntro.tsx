import styles from './StudyIntro.module.scss';

type StudyInfoProps = {
  name: string;
  intro: string;
};

export default function StudyIntro({ name, intro }: StudyInfoProps) {
  return (
    <div className={styles.main}>
      <div className={styles.name}>{name}</div>
      <div className={styles.intro}>{intro}</div>
    </div>
  );
}
