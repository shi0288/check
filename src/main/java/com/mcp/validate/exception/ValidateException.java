package com.mcp.validate.exception;

import com.mcp.validate.BindResult;

/**
 * Created by shiqm on 2017-08-16.
 */
public class ValidateException extends RuntimeException {

    private BindResult bindResult;

    public ValidateException(BindResult bindResult) {
        super(bindResult.getMessage());
        this.bindResult = bindResult;
    }

    public ValidateException(String field, Object validValue, String message) {
        this.bindResult = new BindResult(field, validValue, message);
    }
    public BindResult getBindResult() {
        return bindResult;
    }
}
