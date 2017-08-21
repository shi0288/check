package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class IdCard extends BindingValidator {

    private String regex = "^\\d{15}$|^\\d{17}[\\d,x,X]{1}$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.idCard()) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            return matcher.matches();
        }
        return true;
    }

}
