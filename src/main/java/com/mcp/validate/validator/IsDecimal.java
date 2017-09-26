package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class IsDecimal extends BindingValidator {

    private String regex = "^([1-9]\\d*|[0-9]\\d*\\.[0-9]{1,2})$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.isDecimal()) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            return matcher.matches();
        }
        return true;
    }


}
