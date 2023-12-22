package com.example.lifecaremember.model.enumerate;

public enum EmailDomain {
    GMAIL("gmail.com"),
    NAVER("naver.com"),
    DAUM("daum.net"),
    NATE("nate.com"),
    HOTMAIL("hotmail.com");
    private final String domain;

    EmailDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
}
