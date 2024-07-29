import { useState } from 'react';
import EditorBlock from './EditorBlock';
import styles from './EditorPage.module.scss';

let id = 1;

export default function EditorPage() {
  const [blocks, setBlocks] = useState<number[]>([0]);

  const addBlock = (index: number) => {
    let enter = 0;
    setBlocks((prevBlocks) => {
      const newBlocks = [...prevBlocks];
      enter = newBlocks.indexOf(index);
      newBlocks.splice(enter + 1, 0, id++);

      return newBlocks;
    });
    // const newBlock = document.querySelector(
    //   `.class${enter + 1}`,
    // ) as HTMLElement;
    // newBlock.focus();
  };

  const deleteBlock = (index: number) => {
    setBlocks((prevBlocks) => {
      if (prevBlocks.length === 1) {
        return prevBlocks;
      } else {
        const newBlocks = [...prevBlocks];
        const enter = newBlocks.indexOf(index);
        newBlocks.splice(enter, 1);
        return newBlocks;
      }
    });
  };

  return (
    <div className={styles.main}>
      {blocks.map((index) => (
        <EditorBlock
          key={index}
          index={index}
          addBlock={addBlock}
          deleteBlock={deleteBlock}
        />
      ))}
    </div>
  );
}
