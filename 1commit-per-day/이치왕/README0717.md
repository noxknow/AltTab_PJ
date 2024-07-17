## API Specification

### User Management

- **Login (SNS)**

  - **URI:** `/member/login`
  - **Method:** POST
  - **Description:** Login via GitHub, other OAuth providers may be considered later.

- **Logout**
  - **URI:** `/member/logout`
  - **Method:** GET
  - **Description:** SNS Logout

### Study Management

- **View All Studies**

  - **URI:** `/study`
  - **Method:** GET
  - **Description:** View all joined studies.

- **View Study Main Screen**

  - **URI:** `/study/{studyId}`
  - **Method:** GET
  - **Description:** View main screen of a specific study.

- **Set Favorite Study**

  - **URI:** `/study/like/{studyId}`
  - **Method:** POST
  - **Description:** Set a study as favorite.

- **Create Study**

  - **URI:** `/study`
  - **Method:** POST
  - **Description:** Create a new study.

- **Edit Study Settings**

  - **URI:** `/study/{studyId}/settings`
  - **Methods:** POST, PATCH
  - **Description:** Set or modify basic settings of a study, including team name. Invites via Gmail included.

- **Manage Study Premium Features**
  - **URI:** `/study/{studyId}/premium`
  - **Method:** PATCH
  - **Description:** Set premium features like memory and participant limits.

### Collaboration Tools

- **Problem Information Retrieval**

  - **URI:** `/tool/problem-info`
  - **Method:** POST
  - **Description:** Provide problem information by crawling from a Baekjoon problem link.

- **Code Sharing**

  - **URI:** `/tool/compiler/share`
  - **Method:** POST
  - **Description:** Share code in real-time via a compiler tool.

- **Execute Code**
  - **URI:** `/tool/compiler/execute`
  - **Method:** POST
  - **Description:** Execute code using inputs from the input window and display outputs in the output window.

### Board Management

- **Create Post**

  - **URI:** `/board`
  - **Method:** POST
  - **Description:** Create a new post with mandatory title and content.

- **Like Post**
  - **URI:** `/board/{postId}/like`
  - **Method:** POST
  - **Description:** Like a post, with the ability to unlike by re-clicking.

### Profile Management

- **View Profile**

  - **URI:** `/profile`
  - **Method:** GET
  - **Description:** View basic profile information like profile picture and email.

- **Edit Profile**
  - **URI:** `/profile`
  - **Method:** PATCH
  - **Description:** Modify basic profile information.

### Miscellaneous

- **Study Follow**

  - **URI:** `/studies/follow`
  - **Method:** POST
  - **Description:** Follow a study of interest.

- **Unfollow Study**
  - **URI:** `/studies/unfollow`
  - **Method:** DELETE
  - **Description:** Unfollow a study not of interest anymore.
