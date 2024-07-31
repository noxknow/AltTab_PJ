import React from 'react';

type ImageUploadInputProps = {
  onImageUpload: (file: File) => void;
};

export default function ImageUploadInput({
  onImageUpload,
}: ImageUploadInputProps) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      onImageUpload(file);
    }
  };

  return (
    <div>
      <input type="file" accept="image/*" onChange={handleChange} />
    </div>
  );
}
