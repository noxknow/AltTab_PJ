import React from 'react';
import styles from './Dropdown.module.scss';

type DropdownProps = {
  handleOption: (dropdownOption: string) => void;
  style?: React.CSSProperties;
};

export function Dropdown({ handleOption, style }: DropdownProps) {
  return (
    <div className={styles.main} style={style}>
      <div onClick={() => handleOption('header')}>Header</div>
      <div onClick={() => handleOption('content')}>Content</div>
      <div onClick={() => handleOption('table')}>Table</div>
      <div onClick={() => handleOption('image')}>Image</div>
    </div>
  );
}
