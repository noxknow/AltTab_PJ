import React, { useRef, useState } from 'react';
import ContentEditable, { ContentEditableEvent } from 'react-contenteditable';
import styles from './EditorBlock.module.scss';

type EditorBlockProps = {
  index: number;
  addBlock: (index: number) => void;
  deleteBlock: (index: number) => void;
};

export default function EditorBlock({
  index,
  addBlock,
  deleteBlock,
}: EditorBlockProps) {
  const text = useRef<string>('');
  const [name, setName] = useState<string>('default');

  const handleChange = (e: ContentEditableEvent) => {
    text.current = e.target.value;
  };

  const onKeyDownHandler = (e: React.KeyboardEvent) => {
    if (e.key === '/') {
      setName('zz');
    } else if (e.key === 'Enter') {
      e.preventDefault();
      addBlock(index);
    } else if (e.key === 'Backspace') {
      if (text.current === '') {
        e.preventDefault();
        deleteBlock(index);
      }
    }
  };

  return (
    <ContentEditable
      className={`class${index} ${styles[name]}`}
      html={text.current}
      onChange={handleChange}
      onKeyDown={onKeyDownHandler}
    />
  );
}
