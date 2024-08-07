import React from 'react';
import classNames from 'classnames';
import styles from './DisabledButton.module.scss';

type DisabledButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  color?: string;
};

export function DisabledButton({
  color = 'black',
  ...rest
}: DisabledButtonProps) {
  const { children, className } = rest;

  const buttonClass = classNames(className, styles.button, {
    [styles.green]: color === 'green',
    [styles.black]: color === 'black',
    [styles.blue]: color === 'blue',
    [styles.red]: color === 'red',
  });

  return (
    <button type="button" className={buttonClass} {...rest}>
      {children}
    </button>
  );
}
