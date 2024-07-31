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
  const [showBlock, setShowBlock] = useState(true);
  const [showDropdown, setShowDropdown] = useState(false);
  const [showImageInput, setShowImageInput] = useState(false);

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
    if (dropdownOption === 'image') {
      setShowBlock(false);
      setShowImageInput(true);
    }
    setInnerOption(dropdownOption);
    setShowDropdown(false);
  };

  const handleImageUpload = (file: File) => {
    const reader = new FileReader();
    reader.onloadend = () => {
      if (reader.result) {
        const imageUrl = reader.result as string;
        innerText.current = `<img src="${imageUrl}" alt="image" />`;
        setShowImageInput(false);
        setShowBlock(true);
      }
    };
    reader.readAsDataURL(file);
  };

  const handlePaste = (e: React.ClipboardEvent<HTMLDivElement>) => {
    const items = e.clipboardData.items;
    for (let i = 0; i < items.length; i++) {
      if (items[i].type.startsWith('image/')) {
        setShowBlock(false);
        const file = items[i].getAsFile();
        if (file) {
          handleImageUpload(file);
        }
      }
    }
  };

  return (
    <div onPaste={handlePaste}>
      {showBlock && (
        <ContentEditable
          className={`a${id} ${styles[innerOption]}`}
          html={innerText.current}
          onChange={handleChange}
          onKeyDown={onKeyDownHandler}
        />
      )}
      {showDropdown && <Dropdown handleOption={handleOption} />}
      {showImageInput && (
        <div>
          <input
            type="file"
            accept="image/*"
            onChange={(e) => {
              const file = e.target.files?.[0];
              if (file) {
                handleImageUpload(file);
              }
            }}
          />
        </div>
      )}
    </div>
  );
}
