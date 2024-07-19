## 프로젝트 초기화 후 초기 설정

### dependencies 설치

- `sass`, `eslint`, `prettier` 등 의존성 설치

### vite 절대경로 설정

- vite.config.ts
    - `/src` 경로를 `@`만 입력하여 import 할 수 있도록 설정
    
    ```jsx
    // vite.config.ts
    import { defineConfig } from 'vite';
    import react from '@vitejs/plugin-react-swc';
    import path from 'path';
    import svgr from 'vite-plugin-svgr';
    import tsconfigPaths from 'vite-tsconfig-paths';
    
    // https://vitejs.dev/config/
    export default defineConfig({
    	// tsconfigPaths() 사용 또는 resolve 관련 설정 중 한가지만 적용해도 됩니다.
      plugins: [react(), svgr(), tsconfigPaths()],
      resolve: {
        alias: [{ find: '@', replacement: path.resolve(__dirname, 'src') }],
        extensions: [
          '.js',
          '.ts',
          '.jsx',
          '.tsx',
          '.css',
          '.css.ts',
          '.module.scss',
        ],
      },
    });
    
    ```
    
- tsconfig.json
    
    ```jsx
    // tsconfig.json 파일에 다음 설정 추가
      "compilerOptions": {
        "baseUrl": ".",
        "paths": {
          "@/*": ["src/*"]
        }
      },
    ```
    
- (중요) custom.d.ts
    - src/types/custom.d.ts 파일을 추가하여 @/* 경로로부터 import 한 파일을 모듈로 인식하도록 설정
    
    ```jsx
    // custom.d.ts
    declare module '@/*';
    ```
    

### svg 파일 컴포넌트화

- svg.d.ts
    - src/types/svg.d.ts 파일 생성
        
        ```jsx
        // svg.d.ts
        declare module '*.svg' {
          const value: React.FunctionComponent<React.SVGAttributes<SVGElement>>;
          export default value;
        }
        ```
        
    - svg 파일을 import하여 컴포넌트처럼 사용할 수 있음
        
        ```jsx
        // src/components/Alarm/Alarm.tsx
        import AlarmSVG from '@/assets/icons/alarm.svg?react';
        
        export function Alarm() {
          return <AlarmSVG width={50} height={50} fill={'#fff'} />;
        }
        ```

## DB 이슈

---

```java
com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column 'drawing_data' at row 1
```

MySQL에서 `tinytext` 타입의 컬럼은 최대 255 바이트의 데이터를 저장할 수 있다. 그렇기 때문에 base64로 인코딩된 이미지 데이터는 이 한도를 초과할 가능성이 크다. 이를 해결하려면 `drawing_data` 컬럼의 타입을 `LONGTEXT` 로 변경한다.

## Request Header

---

DB를 해결한 이후 Request Header에 데이터를 넣을 때 발생한 이슈

→ pako 라이브러리를 통해서 String data를 압축 및 해제하여 전송받는다.

```jsx
import pako from 'pako';

const isBase64 = (str: string) => {
  try {
    return btoa(atob(str)) === str;
  } catch (err) {
    return false;
  }
};

const compressData = (data: string) => {
  const base64Data = data.split(',')[1];
  if (!isBase64(base64Data)) {
    throw new Error('Invalid base64 string');
  }
  const binaryString = Uint8Array.from(atob(base64Data), c => c.charCodeAt(0));
  const compressed = pako.deflate(binaryString);
  return btoa(String.fromCharCode.apply(null, Array.from(compressed)));
};

const decompressData = (data: string) => {
  const binaryString = Uint8Array.from(atob(data), c => c.charCodeAt(0));
  const decompressed = pako.inflate(binaryString);
  return `data:image/png;base64,${btoa(String.fromCharCode.apply(null, Array.from(decompressed)))}`;
};
```