package com.ssafy.alttab.common.exception;

public class DocumentNotFoundException extends Exception {

    public DocumentNotFoundException() {
        super("Document 를 찾을 수 없습니다.");
    }
}
