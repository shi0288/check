package com.mcp.validate.validator;

import com.mcp.validate.BindingValidator;
import com.mcp.validate.annotation.Check;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by shiqm on 2017-08-16.
 */
public class Min extends BindingValidator {

    @Override
    public boolean business(Check check, String fieldName, Object value) {
        if (StringUtils.isEmpty(check.min())) {
            return true;
        }
        BigDecimal cond = BigDecimal.valueOf(Double.valueOf(check.min()));
        BigDecimal param = BigDecimal.valueOf(Double.valueOf(value.toString()));
        if (param.compareTo(cond) >= 0) {
            return true;
        }
        this.msg = "#name#不能小于" + check.min();
        return false;
    }


}
