package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.fastcloud.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class NotZero extends BindingValidator {

    private String regex="^\\+?[1-9][0-9]*$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if(check.notZero()){
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            this.msg = "#name#不能为0" ;
            return matcher.matches();
        }
        return true;
    }

}
