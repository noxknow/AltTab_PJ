import pako from 'pako';

// 데이터 압축 함수
export const compressData = (data: string): string => {
    try {
        const compressed = pako.deflate(data);
        return btoa(compressed.reduce((data, byte) => data + String.fromCharCode(byte), ''));
    } catch (error) {
        console.error('Compression error:', error);
        throw error;
    }
};

// 데이터 해제 함수
export const decompressData = (data: string): string => {
  try {
    const binary = atob(data);
    const bytes = new Uint8Array(binary.length);
    for (let i = 0; i < binary.length; i++) {
      bytes[i] = binary.charCodeAt(i);
    }
    return pako.inflate(bytes, { to: 'string' });
  } catch (error) {
    console.error('Decompression error:', error);
    throw error;
  }
};