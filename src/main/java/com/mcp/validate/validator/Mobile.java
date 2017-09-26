package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.fastcloud.annotation.Check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class Mobile extends BindingValidator {

    private String regex="^1[3|4|5|7|8][0-9]\\d{8}$";

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if(check.mobile()){
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(String.valueOf(value));
            return matcher.matches();
        }
        return true;
    }

    public static void main(String[] args) {
        String v="10000000022";
        Pattern pattern = Pattern.compile("^([1-9]\\d*|[0-9]\\d*\\.[0-9]{1,2})$");
        Matcher matcher = pattern.matcher(String.valueOf(v));
        System.out.println(matcher.matches());

    }

}
