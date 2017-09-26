package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class IP extends BindingValidator {

    private String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    private String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (check.ip()) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            return matcher.matches();
        }
        return true;
    }


}
