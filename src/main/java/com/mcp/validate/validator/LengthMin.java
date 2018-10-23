package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;

/**
 * Created by shiqm on 2017-08-21.
 */
public class LengthMin extends BindingValidator {

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.lengthMin() > 0 && String.valueOf(value).length() < check.lengthMin()) {
            this.msg = "#name#长度不能小于" + check.lengthMin();
            return false;
        } else {
            return true;
        }
    }
}
