import React, { useRef, useState } from 'react';
import ContentEditable, { ContentEditableEvent } from 'react-contenteditable';
import styles from './EditorBlock.module.scss';

type EditorBlockProps = {
  id: string;
  text: string;
  option: string;
  addBlock: (id: string) => void;
  deleteBlock: (id: string) => void;
};

export default function EditorBlock({
  id,
  text,
  option,
  addBlock,
  deleteBlock,
}: EditorBlockProps) {
  const innerText = useRef<string>(text);
  const [innerOption, setInnerOption] = useState(option);
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
      setInnerOption('zz');
      return;
    }
  };
  return (
    <ContentEditable
      className={`a${id} ${styles[innerOption]}`}
      html={innerText.current}
      onChange={handleChange}
      onKeyDown={onKeyDownHandler}
    />
  );
}
