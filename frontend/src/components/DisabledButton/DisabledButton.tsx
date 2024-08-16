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
    [styles.yellow]: color === 'yellow',
    [styles.purple]: color === 'purple',
    [styles.orange]: color === 'orange',
    [styles.pink]: color === 'pink',
    [styles.teal]: color === 'teal',
    [styles.indigo]: color === 'indigo',
    [styles.cyan]: color === 'cyan',
    [styles.lime]: color === 'lime',
    [styles.bronze]: color === 'bronze',
    [styles.silver]: color === 'silver',
    [styles.gold]: color === 'gold',
    [styles.platinum]: color === 'platinum',
    [styles.diamond]: color === 'diamond',
    [styles.ruby]: color === 'ruby',
    [styles.gray]: color === 'gray',
  });

  return (
    <button type="button" className={buttonClass} {...rest}>
      {children}
    </button>
  );
}
