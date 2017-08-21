package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;

/**
 * Created by shiqm on 2017-08-21.
 */
public class Length extends BindingValidator {

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.length() > 0 && String.valueOf(value).length() < check.length()) {
            this.msg = "#name#长度不能小于" + check.length();
            return false;
        } else {
            return true;
        }
    }
}
