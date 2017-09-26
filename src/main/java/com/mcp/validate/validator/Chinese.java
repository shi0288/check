package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class Chinese extends BindingValidator {

    private String regex = "^[\\u4e00-\\u9fa5]+$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.chinese()) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            this.msg = "#name#必须为中文";
            return matcher.matches();
        }
        return true;
    }


}
