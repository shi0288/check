package com.mcp.validate;

import com.mcp.fastcloud.annotation.Check;

/**
 * Created by shiqm on 2017-08-16.
 */
public interface BaseValidator {

    void validate(Check check, String fieldName, Object value);

}
