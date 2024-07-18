## 커밋 컨벤션

---

1. **커밋 메시지 구조**:
    - `타입(class명) : 커밋 메시지 #이슈번호`
2. **타입**:
    - `feat`: 새로운 기능 추가 및 수정
    - `fix`: 버그 수정
    - `docs`: 문서 수정
    - `style`: 코드 스타일 변경 (포맷, 세미콜론 추가 등)
    - `design` : 레이아웃, 스타일 수정
    - `refactor`: 코드 리팩토링 (기능 변경 없음)
    - `test`: 테스트 추가 및 수정
    - `chore`: 빌드 작업 또는 보조 도구 수정
3. **예시**:
    - `feat(login): 로그인 기능 추가 #SP1A23`

## 브랜치 컨벤션

---

1. **브랜치 네이밍 규칙**:
    - `타입/이슈번호`
2. **타입**:
    - `feature`: 새로운 기능 개발
    - `develop`: 개발 중인 버전
3. **예시**:
    - `feature/be-login/S11P12A309-105`
    - `feature/fe-header/S11P12A309-107`

## PR 컨벤션

---

1. **PR 제목**:
    - `타입(class명) : 커밋 메시지 #이슈번호`
2. **PR 본문**:
    - **변경 사항**: 변경된 주요 사항 설명
    - **왜 필요한가?**: 변경이 필요한 이유 설명
    - **체크리스트**:
        - [ ]  코드가 올바르게 작동하는지 확인
        - [ ]  새로운 테스트가 추가되었거나 기존 테스트가 수정되었는지 확인
        - [ ]  관련 문서가 업데이트 되었는지 확인
3. **예시**:
    
    ```markdown
    ### 변경 사항
    로그인 기능을 추가했습니다.
    
    ### 왜 필요한가?
    사용자 인증을 위해 로그인 기능이 필요합니다.
    
    ### 체크리스트
    - [x] 코드가 올바르게 작동하는지 확인
    - [x] 새로운 테스트가 추가되었거나 기존 테스트가 수정되었는지 확인
    - [x] 관련 문서가 업데이트 되었는지 확인
    ```
    

# 코드 컨벤션

---

## 프론트엔드

- 어느 정도 맞출 수 있는 건 eslint, prettier, ~~stylelint~~로 세팅하기
    - import/export 순서
        
        ```jsx
        import { a, b, c } from "ddd";
        
        export * from "a";
        export * from "b";
        ```
        
    - pre-commit 툴 (husky)

### 변수

- **`camelCase`**로 작성해 주세요.
- ‘대상’의 의미가 잘 표현되도록 **명사**로 지어주세요.
- 최대한 축약을 피하고 자세히 작성해 주세요.
    - btn (x) button (o)
    - idx (x) index (o)
- 매직넘버 사용을 **지양**하고, 상수의 경우 대문자 및 **`SNAKE_CASE`**로 선언해주세요.
    
    ```jsx
    // Bad
    setTimeOut(() => {}, 1000)
    
    // Good
    const TIME_INTERVAL = 1000;
    setTimeOut(() => {}, TIME_INTERVAL);
    ```
    
- Boolean 변수의 경우 **수동태(과거분사)** 형태로 작성해 주세요. ⇒ `selected`

### 함수

- **`camelCase`**로 작성해 주세요.
- ‘동작’의 의미가 잘 표현되도록 **동사**로 시작해 주세요.
    - 반환값이 Boolean 타입이라면 be동사로 시작하기
    - ⇒ `isCurrentUser()`, `hasItem()`, `shouldFetching()`
- 이벤트 핸들러는 ‘**핸들러 함수 이름 + 대상 컴포넌트명**’ 형태로 지어 주세요.
    - ⇒ `<button onClick={**onClick**}>다음</button>`
    - ⇒ `<button onClick={**onClickUploadButton**}>사진 추가</button>`

### 기타

- 디렉토리명은 **복수형**, 소문자 및 **`kebab-case`**로 지어주세요. ⇒ `components`, `hooks`
- 클래스 및 컴포넌트명은 **`PascalCase`**로 지어주세요. ⇒ `UserListItem`
    - 컴포넌트가 디렉토리로 표현된 경우에도 동일
    - components/Button
- URL, API, HTML와 같은 대문자 약어는 대문자 그대로 사용해요.
    - handleUrl (x) handleURL (o)
- 페어 프로그래밍(공동 작업) 한 경우 커밋 메시지의 body에 다음을 추가한다.
    
    ```
    Co-authored-by: LEEJW1953 <jiwonlee1953@naver.com>
    Co-authored-by: js43o <js43o@naver.com>
    Co-authored-by: ttaerrim <ltr0121@naver.com>
    ```
    

### ✏️ 코딩 스타일

- 변수 선언 시 `let` 보다는 `const`를 우선적으로 사용해 주세요.
    - 일단 `const`로 선언하고 변경할 일이 생겼을 때 `let`으로 바꾸는 것도 좋음!
- 컴포넌트 작성 시에는 **함수 선언식** **`function() { ... }`**을 사용해 주세요.
- 로직 작성 시에는 **화살표 함수** `() => {}`를 사용해 주세요.
    - 화살표 함수는 별도의 this 바인딩 없이 상위 컨텍스트에 바인딩되기 때문에 함수 표현식보다 혼란이 적으며 덜 장황하고 추론이 쉽다, 암시적 반환 활용이 가능하다
- 비동기 처리는 가능한 `async-await` 문법을 사용해 주세요.
- 단순히 값을 결정하는 분기문은 삼항 연산자 `a ? b : c` 사용해 주세요.
- 구조 분해 할당을 적극 사용해요.
    - 새로운 이름으로 변수에 할당할 때는 꼭 구조 분해 할당을 사용하지 않아도 돼요.
- 변수 조합으로 문자열을 만들 때는 템플릿 문자열을 사용해요.
- 한 줄짜리 블록에도 {}를 사용해요.
- 한 파일에는 하나의 컴포넌트만 작성해요.
- 컴포넌트를 export 할 때는 함수 작성 시 함께 export 해요.

### TypeScript

- interface가 아닌 **type**을 사용해요.
- 컴포넌트 props type은 ComponentProps 네이밍을 사용합니다.
    - InputProps, ButtonProps…