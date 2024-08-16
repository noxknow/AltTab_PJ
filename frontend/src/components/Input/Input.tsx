import styles from './Input.module.scss';

export function Input({ ...props }) {
  const { type = 'text' } = props;
  return <input type={type} className={styles.container} {...props} />;
}
