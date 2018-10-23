package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;

/**
 * Created by shiqm on 2017-08-21.
 */
public class LengthMax extends BindingValidator {

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.lengthMax() > 0 && String.valueOf(value).length() > check.lengthMax()) {
            this.msg = "#name#长度不能大于" + check.lengthMax();
            return false;
        } else {
            return true;
        }
    }
}
