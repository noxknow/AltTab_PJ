import React, { useRef, useState } from 'react';
import ContentEditable, { ContentEditableEvent } from 'react-contenteditable';
import styles from './EditorBlock.module.scss';
import { Dropdown } from './Dropdown';
import { TableInput } from './TableInput';
import { ImageUploadInput } from './ImageUploadInput';
import DraggableSVG from '@/assets/icons/draggable.svg?react';

type EditorBlockProps = {
  id: string;
  text: string;
  option: string;
  addBlock: (id: string) => void;
  deleteBlock: (id: string) => void;
  updateBlock: (id: string, text: string, option: string) => void;
};

export function EditorBlock({
  id,
  text,
  option,
  addBlock,
  deleteBlock,
  updateBlock,
}: EditorBlockProps) {
  const innerText = useRef<string>(text);
  const [innerOption, setInnerOption] = useState(option);
  const [showBlock, setShowBlock] = useState(true);
  const [showDropdown, setShowDropdown] = useState(false);
  const [showImageInput, setShowImageInput] = useState(false);
  const [showTableInput, setShowTableInput] = useState(false);
  const [dropdownPosition, setDropdownPosition] = useState<{
    top: number;
    left: number;
  } | null>(null);
  const isFocusedRef = useRef(false);

  const handleChange = (e: ContentEditableEvent) => {
    innerText.current = e.target.value;
    if (
      (innerText.current === '' && innerOption === 'image') ||
      (innerText.current === '<br>' && innerOption === 'table')
    ) {
      setInnerOption('content');
      deleteBlock(id);
    }
  };

  const handleFocus = () => {
    isFocusedRef.current = true;
  };

  const handleBlur = () => {
    if (isFocusedRef.current) {
      updateBlock(id, innerText.current, innerOption);
      isFocusedRef.current = false;
    }
  };
  const handleMouseDown = () => {
    updateBlock(id, innerText.current, innerOption);
  };

  const onKeyDownHandler = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      setShowDropdown(false);
      e.preventDefault();
      addBlock(id);
    } else if (e.key === 'Backspace') {
      setShowDropdown(false);
      if (innerText.current === '') {
        e.preventDefault();
        deleteBlock(id);
        setInnerOption('content');
      }
      // if (innerOption === 'table' || innerOption === 'image') {
      //   e.repeat;
      //   if (innerText.current === '') {
      //     console.log('hi');
      //     deleteBlock(id);
      //     setInnerOption('content');
      //   }
      // }
    } else if (e.key === '/') {
      e.preventDefault();
      if (showDropdown) {
        setShowDropdown(false);
      } else {
        setDropdownPosition(getCursorPosition());
        setShowDropdown(true);
      }
    } else if (innerOption === 'table' || innerOption === 'image') {
      return;
    } else if (e.key === 'ArrowUp') {
      setShowDropdown(false);
      e.preventDefault();
      move('up');
    } else if (e.key === 'ArrowDown') {
      setShowDropdown(false);
      e.preventDefault();
      move('down');
    }
  };

  const getCursorPosition = () => {
    const selection = window.getSelection();
    if (!selection || selection.rangeCount === 0) return null;

    const range = selection.getRangeAt(0);
    let rect = range.getClientRects()[0];

    if (!rect) {
      const span = document.createElement('span');
      range.insertNode(span);
      rect = span.getClientRects()[0];
      span.remove();
    }

    return {
      top: rect.top - 55,
      left: rect.left,
    };
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

  const move = (direction: string) => {
    const selection = document.getSelection();
    const newRange = document.createRange();
    const curNode = document.querySelector(`.a${id}`);

    let index = 0;
    while (
      curNode !==
      document.querySelector('.block')!.childNodes[index].childNodes[0]
        .childNodes[1].childNodes[0]
    ) {
      index++;
    }

    if (direction === 'up' && index === 0) {
      return;
    }

    if (
      direction === 'down' &&
      index === document.querySelector('.block')!.childNodes.length - 1
    ) {
      return;
    }

    let startContainer = null;

    if (direction === 'up') {
      startContainer =
        document.querySelector('.block')!.childNodes[index - 1].childNodes[0]
          .childNodes[1].childNodes[0];
    } else {
      startContainer =
        document.querySelector('.block')!.childNodes[index + 1].childNodes[0]
          .childNodes[1].childNodes[0];
    }

    if (startContainer && selection) {
      const pos = selection.anchorOffset;

      if (startContainer.textContent && startContainer.firstChild) {
        if (startContainer.firstChild.nodeName === 'TABLE') {
          newRange.setStart(startContainer.firstChild, 0);
        } else {
          newRange.setStart(
            startContainer.firstChild,
            pos > startContainer.textContent.length
              ? startContainer.textContent.length
              : pos,
          );
        }
      } else {
        newRange.setStart(startContainer, 0);
      }

      selection.removeAllRanges();
      selection.addRange(newRange);
    }
  };

  return (
    <div className={styles.main}>
      <div className={styles.svg} onMouseDown={handleMouseDown}>
        <DraggableSVG />
      </div>
      <div onPaste={handlePaste} className={styles.block}>
        {showBlock && (
          <ContentEditable
            className={`a${id} ${styles[innerOption]}`}
            html={innerText.current}
            onChange={handleChange}
            onKeyDown={onKeyDownHandler}
            onFocus={handleFocus}
            onBlur={handleBlur}
          />
        )}
        {showDropdown && dropdownPosition && (
          <Dropdown
            handleOption={handleOption}
            style={{
              position: 'absolute',
              top: dropdownPosition.top,
              left: dropdownPosition.left,
            }}
          />
        )}
        {showImageInput && (
          <ImageUploadInput onImageUpload={handleImageUpload} />
        )}
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
    </div>
  );
}
