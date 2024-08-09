import { useState } from 'react';

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

  return (
    <div>
      <label>
        Rows:
        <input
          type="number"
          value={rows}
          onChange={(e) => setRows(Number(e.target.value))}
          min="1"
        />
      </label>
      <label>
        Columns:
        <input
          type="number"
          value={columns}
          onChange={(e) => setColumns(Number(e.target.value))}
          min="1"
        />
      </label>
      <button onClick={handleCreateTable}>Create Table</button>
      <button onClick={onCancel}>Cancel</button>
    </div>
  );
}
