import codeHighlighter from 'highlight.js/lib/core';

import java from 'highlight.js/lib/languages/java';

codeHighlighter.registerLanguage('java', java);

export { codeHighlighter };
