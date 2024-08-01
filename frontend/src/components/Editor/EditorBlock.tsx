import React, { useRef, useState } from 'react';
import ContentEditable, { ContentEditableEvent } from 'react-contenteditable';
import styles from './EditorBlock.module.scss';
import Dropdown from './Dropdown';
import TableInput from './TableInput';
import ImageUploadInput from './ImageUploadInput';

type EditorBlockProps = {
  id: string;
  text: string;
  option: string;
  addBlock: (id: string) => void;
  deleteBlock: (id: string) => void;
  moveUp: (id: string) => void;
  moveDown: (id: string) => void;
};

const EditorBlock: React.FC<EditorBlockProps> = ({
  id,
  text,
  option,
  addBlock,
  deleteBlock,
  moveUp,
  moveDown,
}) => {
  const innerText = useRef<string>(text);
  const [innerOption, setInnerOption] = useState(option);
  const [showBlock, setShowBlock] = useState(true);
  const [showDropdown, setShowDropdown] = useState(false);
  const [showImageInput, setShowImageInput] = useState(false);
  const [showTableInput, setShowTableInput] = useState(false);

  const handleChange = (e: ContentEditableEvent) => {
    innerText.current = e.target.value;
  };

  const onKeyDownHandler = (e: React.KeyboardEvent) => {
    if (innerOption === 'table') {
      return;
    }

    if (e.key === 'Enter') {
      e.preventDefault();
      addBlock(id);
    } else if (e.key === 'Backspace') {
      if (innerText.current === '') {
        e.preventDefault();
        deleteBlock(id);
      }
    } else if (e.key === '/') {
      e.preventDefault();
      setShowDropdown(true);
    } else if (e.key === 'ArrowUp') {
      e.preventDefault();
      moveUp(id);
    } else if (e.key === 'ArrowDown') {
      e.preventDefault();
      moveDown(id);
    }
  };

  const handleOption = (dropdownOption: string) => {
    if (dropdownOption === 'image') {
      setShowBlock(false);
      setShowImageInput(true);
    } else if (dropdownOption === 'table') {
      setShowBlock(false);
      setShowTableInput(true);
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

  const handleTableCreate = (rows: number, columns: number) => {
    let tableHTML = '<table>';
    for (let i = 0; i < rows; i++) {
      tableHTML += '<tr>';
      for (let j = 0; j < columns; j++) {
        tableHTML += '<td>0</td>';
      }
      tableHTML += '</tr>';
    }
    tableHTML += '</table>';
    innerText.current = tableHTML;
    setShowTableInput(false);
    setShowBlock(true);
  };

  return (
    <div onPaste={handlePaste} className={styles.block}>
      {showBlock && (
        <ContentEditable
          className={`a${id} ${styles[innerOption]} `}
          html={innerText.current}
          onChange={handleChange}
          onKeyDown={onKeyDownHandler}
        />
      )}
      {showDropdown && <Dropdown handleOption={handleOption} />}
      {showImageInput && <ImageUploadInput onImageUpload={handleImageUpload} />}
      {showTableInput && (
        <TableInput
          onCreateTable={handleTableCreate}
          onCancel={() => {
            setShowTableInput(false);
            setShowBlock(true);
          }}
        />
      )}
    </div>
  );
};

export default EditorBlock;
