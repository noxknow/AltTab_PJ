import styles from './Input.module.scss';

type InputProps = {
  placeholder?: string;
  maxLength?: number;
  type?: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

export function Input({ type = 'text', placeholder, maxLength, value, onChange }: InputProps) {
  return (
    <input
      type={type}
      className={styles.container}
      placeholder={placeholder}
      maxLength={maxLength}
      value={value}
      onChange={onChange}
    />
  );
}