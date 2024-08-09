import { Section } from './Section';
import styles from './Content.module.scss';

export function Content() {
  return (
    <div className={styles.content}>
      <Section title="AI 추천 문제" />
      <Section title="약점 보완" />
      <Section title="강점 강화" />
    </div>
  );
}
