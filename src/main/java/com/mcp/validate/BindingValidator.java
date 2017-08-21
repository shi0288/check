package com.mcp.validate;


import com.mcp.validate.annotation.Check;
import com.mcp.validate.exception.ValidateException;

/**
 * Created by shiqm on 2017-08-16.
 */

public abstract class BindingValidator implements BaseValidator {

    protected String msg;

    @Override
    public void validate(Check check, String fieldName, Object value) {
        if (value != null) {
            if (!business(check, fieldName, value)) {
                throw new ValidateException(this.error(check.name(), fieldName, value));
            }
        }
    }

    public abstract boolean business(Check check, String fieldName, Object value);

    private BindResult error(String name, String fieldName, Object value) {
        String message = this.getMsg().replaceAll("#field#", fieldName == null ? "" : fieldName);
        message = message.replaceAll("#value#", value == null ? "" : String.valueOf(value));
        message = message.replaceAll("#name#", name == null ? "" : name);
        return new BindResult(fieldName, value, message);
    }

    public String getMsg() {
        if (this.msg == null) {
            this.setMsg("#name#格式不正确");
        }
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
