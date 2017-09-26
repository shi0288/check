package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.fastcloud.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class NumOrLetter extends BindingValidator {

    private String regex = "^[a-zA-Z0-9]+$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.numOrLetter()) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            this.msg = "#name#必须为数字或数字" ;
            return matcher.matches();
        }
        return true;
    }

}
