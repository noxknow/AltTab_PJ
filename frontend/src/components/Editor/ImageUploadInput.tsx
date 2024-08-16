import React from 'react';
import styles from './ImageUploadInput.module.scss';

type ImageUploadInputProps = {
  onImageUpload: (file: File) => void;
};

export function ImageUploadInput({ onImageUpload }: ImageUploadInputProps) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      onImageUpload(file);
    }
  };

  return (
    <div className={styles.imageUploadInput}>
      <label htmlFor="fileUpload" className={styles.labelText}>
        Upload Image
        <input
          id="fileUpload"
          type="file"
          accept="image/*"
          className={styles.fileInput}
          onChange={handleChange}
        />
      </label>
    </div>
  );
}
