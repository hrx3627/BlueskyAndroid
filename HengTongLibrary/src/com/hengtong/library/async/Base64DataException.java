package com.hengtong.library.async;

import java.io.IOException;

public class Base64DataException extends IOException {
    /**
	 * serialVersionUID：
	 */
	private static final long serialVersionUID = 7271343334859676104L;

	public Base64DataException(String detailMessage) {
        super(detailMessage);
    }
}