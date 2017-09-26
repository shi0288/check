package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.fastcloud.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class Letter extends BindingValidator {

    private String regex = "^[A-Za-z]+$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.letter()) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            this.msg = "#name#必须为字母" ;
            return matcher.matches();
        }
        return true;
    }


}
