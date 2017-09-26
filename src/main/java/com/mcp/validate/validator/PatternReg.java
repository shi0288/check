package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shiqm on 2017-08-21.
 */
public class PatternReg extends BindingValidator {

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if(!StringUtils.isEmpty(check.pattern())){
            Pattern pattern = Pattern.compile(check.pattern());
            Matcher matcher = pattern.matcher(String.valueOf(value));
            return matcher.matches();
        }
        return true;
    }
}
