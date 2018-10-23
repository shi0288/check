package com.mcp.validate.resolver;

import com.mcp.validate.BaseValidator;
import com.mcp.validate.BindResult;
import com.mcp.validate.ValidatorCache;
import com.mcp.validate.annotation.Check;
import com.mcp.validate.exception.ValidateException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shiqm on 2017-08-18.
 */
public class CheckMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final Map<MethodParameter, CheckMethodArgumentResolver.CheckNamedValueInfo> namedValueInfoCache = new ConcurrentHashMap(256);
    private final ConfigurableBeanFactory configurableBeanFactory;
    private final BeanExpressionContext expressionContext;


    public CheckMethodArgumentResolver() {
        this.configurableBeanFactory = null;
        this.expressionContext = null;
    }

    public CheckMethodArgumentResolver(ConfigurableBeanFactory configurableBeanFactory) {
        this.configurableBeanFactory = configurableBeanFactory;
        this.expressionContext = configurableBeanFactory != null ? new BeanExpressionContext(configurableBeanFactory, new RequestScope()) : null;

    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(Check.class)) {
            if (Map.class.isAssignableFrom(methodParameter.nestedIfOptional().getNestedParameterType())) {
                String paramName = ((Check) methodParameter.getParameterAnnotation(Check.class)).value();
                return StringUtils.hasText(paramName);
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        CheckMethodArgumentResolver.CheckNamedValueInfo namedValueInfo = this.getNamedValueInfo(methodParameter);
        MethodParameter nestedParameter = methodParameter.nestedIfOptional();
        Object resolvedName = this.resolveStringValue(namedValueInfo.name);
        if (resolvedName == null) {
            throw new IllegalArgumentException("Specified name must not resolve to null: [" + namedValueInfo.name + "]");
        } else {
            Object arg = this.resolveName(resolvedName.toString(), nestedParameter, nativeWebRequest);
            if (arg == null) {
                if (namedValueInfo.defaultValue != null) {
                    arg = this.resolveStringValue(namedValueInfo.defaultValue);
                } else if (namedValueInfo.required && !nestedParameter.isOptional()) {
                    this.handleMissingValue(namedValueInfo.name, nestedParameter, nativeWebRequest);
                }
                arg = this.handleNullValue(namedValueInfo.name, arg, nestedParameter.getNestedParameterType());
            } else if ("".equals(arg) && namedValueInfo.defaultValue != null) {
                arg = this.resolveStringValue(namedValueInfo.defaultValue);
            }
            if (webDataBinderFactory != null) {
                WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, (Object) null, namedValueInfo.name);
                try {
                    arg = binder.convertIfNecessary(arg, methodParameter.getParameterType(), methodParameter);
                } catch (ConversionNotSupportedException var11) {
                    throw new MethodArgumentConversionNotSupportedException(arg, var11.getRequiredType(), namedValueInfo.name, methodParameter, var11.getCause());
                } catch (TypeMismatchException var12) {
                    throw new MethodArgumentTypeMismatchException(arg, var12.getRequiredType(), namedValueInfo.name, methodParameter, var12.getCause());
                }
            }
            Check check = methodParameter.getParameterAnnotation(Check.class);
            if (check.required() && (arg == null || "".equals(arg))) {
                String message;
                if (check.name().equals("")) {
                    message = namedValueInfo.name;
                } else {
                    message = check.name();
                }
                message += "不能为空";
                BindResult bindResult = new BindResult(namedValueInfo.name, message);
                throw new ValidateException(bindResult);
            }
            if (check.valid().length >= 0) {
                for (int i = 0; i < check.valid().length; i++) {
                    Class clazz = check.valid()[i];
                    if (BaseValidator.class.isAssignableFrom(clazz)) {
                        Method method = ReflectionUtils.findMethod(clazz, "validate", Check.class, String.class, Object.class);
                        if (method != null) {
                            Object[] params = new Object[3];
                            params[0] = check;
                            params[1] = namedValueInfo.name;
                            params[2] = arg;
                            ReflectionUtils.invokeMethod(method, clazz.newInstance(), params);
                        }
                    }
                }
            }
            for (int i = 0; i < ValidatorCache.get().size(); i++) {
                BaseValidator BaseValidator = ValidatorCache.get(i);
                BaseValidator.validate(check, namedValueInfo.name, arg);
            }
            return arg;
        }
    }

    private Object resolveStringValue(String value) {
        if (this.configurableBeanFactory == null) {
            return value;
        } else {
            String placeholdersResolved = this.configurableBeanFactory.resolveEmbeddedValue(value);
            BeanExpressionResolver exprResolver = this.configurableBeanFactory.getBeanExpressionResolver();
            return exprResolver == null ? value : exprResolver.evaluate(placeholdersResolved, this.expressionContext);
        }
    }

    private Object handleNullValue(String name, Object value, Class<?> paramType) {
        if (value == null) {
            if (Boolean.TYPE.equals(paramType)) {
                return Boolean.FALSE;
            }
            if (paramType.isPrimitive()) {
                throw new IllegalStateException("Optional " + paramType.getSimpleName() + " parameter '" + name + "' is present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
            }
        }
        return value;
    }

    protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        BindResult bindResult = new BindResult(name, "缺少参数:" + name);
        throw new ValidateException(bindResult);
    }


    protected CheckNamedValueInfo createNamedValueInfo(MethodParameter methodParameter) {
        Check ann = (Check) methodParameter.getParameterAnnotation(Check.class);
        return new CheckMethodArgumentResolver.CheckNamedValueInfo(ann);
    }

    private CheckMethodArgumentResolver.CheckNamedValueInfo updateNamedValueInfo(MethodParameter parameter, CheckMethodArgumentResolver.CheckNamedValueInfo info) {
        String name = info.name;
        if (info.name.length() == 0) {
            name = parameter.getParameterName();
            if (name == null) {
                throw new IllegalArgumentException("Name for argument type [" + parameter.getNestedParameterType().getName() + "] not available, and parameter name information not found in class file either.");
            }
        }
        String defaultValue = "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n".equals(info.defaultValue) ? null : info.defaultValue;
        return new CheckMethodArgumentResolver.CheckNamedValueInfo(name, info.required, defaultValue);
    }


    private CheckMethodArgumentResolver.CheckNamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        CheckMethodArgumentResolver.CheckNamedValueInfo namedValueInfo = (CheckMethodArgumentResolver.CheckNamedValueInfo) this.namedValueInfoCache.get(parameter);
        if (namedValueInfo == null) {
            namedValueInfo = this.createNamedValueInfo(parameter);
            namedValueInfo = this.updateNamedValueInfo(parameter, namedValueInfo);
            this.namedValueInfoCache.put(parameter, namedValueInfo);
        }
        return namedValueInfo;
    }

    protected Object resolveName(String s, MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception {
        HttpServletRequest servletRequest = (HttpServletRequest) nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest.class);
        Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(s, methodParameter, servletRequest);
        if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
            return mpArg;
        } else {
            Object arg = null;
            if (multipartRequest != null) {
                List<MultipartFile> files = multipartRequest.getFiles(s);
                if (!files.isEmpty()) {
                    arg = files.size() == 1 ? files.get(0) : files;
                }
            }
            if (arg == null) {
                String[] paramValues = nativeWebRequest.getParameterValues(s);
                if (paramValues != null) {
                    arg = paramValues.length == 1 ? paramValues[0] : paramValues;
                }
            }
            return arg;
        }
    }

    private static class CheckNamedValueInfo {
        private final String name;
        private final boolean required;
        private final String defaultValue;

        public CheckNamedValueInfo() {
            this.name = "";
            this.required = false;
            this.defaultValue = null;
        }

        public CheckNamedValueInfo(String name, boolean required, String defaultValue) {
            this.name = name;
            this.required = required;
            this.defaultValue = defaultValue;
        }

        public CheckNamedValueInfo(Check annotation) {
            this(annotation.value(), annotation.required(), annotation.defaultValue());
        }

    }

}
