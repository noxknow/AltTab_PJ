import styles from './Dropdown.module.scss';

type DropdownProps = {
  handleOption: (dropdownOption: string) => void;
};

export default function Dropdown({ handleOption }: DropdownProps) {
  return (
    <div className={styles.main}>
      <div onClick={() => handleOption('header')}>Header</div>
      <div onClick={() => handleOption('content')}>Content</div>
      <div onClick={() => handleOption('table')}>Table</div>
      <div onClick={() => handleOption('image')}>Image</div>
    </div>
  );
}
