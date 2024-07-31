import React, { useRef, useState } from 'react';
import ContentEditable, { ContentEditableEvent } from 'react-contenteditable';
import styles from './EditorBlock.module.scss';
import Dropdown from './Dropdown';

type EditorBlockProps = {
  id: string;
  text: string;
  option: string;
  addBlock: (id: string) => void;
  deleteBlock: (id: string) => void;
  moveUp: (id: string) => void;
  moveDown: (id: string) => void;
};

export default function EditorBlock({
  id,
  text,
  option,
  addBlock,
  deleteBlock,
  moveUp,
  moveDown,
}: EditorBlockProps) {
  const innerText = useRef<string>(text);
  const [innerOption, setInnerOption] = useState(option);
  const [showDropdown, setShowDropdown] = useState(false);

  const handleChange = (e: ContentEditableEvent) => {
    innerText.current = e.target.value;
  };
  const onKeyDownHandler = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      addBlock(id);
      return;
    } else if (e.key === 'Backspace') {
      if (innerText.current === '') {
        e.preventDefault();
        deleteBlock(id);
        return;
      }
    } else if (e.key === '/') {
      e.preventDefault();
      setShowDropdown(true);
      // setInnerOption('zz');
      return;
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      moveUp(id);
      return;
    } else if (e.key === 'ArrowDown') {
      e.preventDefault();
      moveDown(id);
      return;
    }
  };
  const handleOption = (dropdownOption: string) => {
    setInnerOption(dropdownOption);
  };
  return (
    <div>
      <ContentEditable
        className={`a${id} ${styles[innerOption]}`}
        html={innerText.current}
        onChange={handleChange}
        onKeyDown={onKeyDownHandler}
      />
      {showDropdown && <Dropdown handleOption={handleOption} />}
    </div>
  );
}
