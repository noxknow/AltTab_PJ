import styles from './Input.module.scss';

type InputProps = {
  type?: string;
  placeholder?: string;
  maxLength?: number;
};

export function Input({ type = 'text', placeholder, maxLength }: InputProps) {
  return (
    <input
      type={type}
      className={styles.container}
      placeholder={placeholder}
      maxLength={maxLength}
    />
  );
}
