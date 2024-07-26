export type requestCompiler = {
  code: string;
  input: string;
};

export type responseCompiler = {
  output: string | null;
  errorMessage: string | null;
};
