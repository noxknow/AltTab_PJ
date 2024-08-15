import classNames from 'classnames';
import styles from './Button.module.scss';

type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  color: 'green' | 'black' | 'red';
  fill: boolean;
  size: 'large' | 'small' | 'long';
};

export function Button({ color, fill, size, ...rest }: ButtonProps) {
  const { type = 'button', disabled = false, children, className } = rest;
  const buttonClass = classNames(className, styles.button, {
    [styles.green]: color === 'green',
    [styles.black]: color === 'black',
    [styles.red]: color === 'red',
    [styles.empty]: !fill,
    [styles.large]: size === 'large',
    [styles.small]: size === 'small',
    [styles.long]: size === 'long',
    [styles.disabled]: disabled,
  });

  return (
    <>
      <button type={type} disabled={disabled} {...rest} className={buttonClass}>
        {children}
      </button>
    </>
  );
}
