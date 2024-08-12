import { useState } from 'react';
import styles from './TableInput.module.scss';

type TableInputProps = {
  onCreateTable: (rows: number, columns: number) => void;
  onCancel: () => void;
};

export function TableInput({ onCreateTable, onCancel }: TableInputProps) {
  const [rows, setRows] = useState(2);
  const [columns, setColumns] = useState(2);

  const handleCreateTable = () => {
    onCreateTable(rows, columns);
  };

  const handleRowsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = Number(e.target.value);
    if (value >= 1 && value <= 10) {
      setRows(value);
    } else if (value < 1) {
      setRows(1);
    } else {
      setRows(10);
    }
  };

  const handleColumnsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = Number(e.target.value);
    if (value >= 1 && value <= 10) {
      setColumns(value);
    } else if (value < 1) {
      setColumns(1);
    } else {
      setColumns(10);
    }
  };

  return (
    <div className={styles.tableInputContainer}>
      <div className={styles.item}>
        <label className={styles.label}>
          Rows:
          <input
            type="number"
            value={rows}
            onChange={handleRowsChange}
            min="1"
            max="10"
            className={styles.input}
          />
        </label>
        <label className={styles.label}>
          Columns:
          <input
            type="number"
            value={columns}
            onChange={handleColumnsChange}
            min="1"
            max="10"
            className={styles.input}
          />
        </label>
      </div>
      <div className={styles.item}>
        <button
          onClick={handleCreateTable}
          className={`${styles.button} ${styles.createButton}`}
        >
          Create Table
        </button>
        <button
          onClick={onCancel}
          className={`${styles.button} ${styles.cancelButton}`}
        >
          Cancel
        </button>
      </div>
    </div>
  );
}
