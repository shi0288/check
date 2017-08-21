package com.mcp.validate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Map;
import java.util.Set;

/**
 * Created by shiqm on 2017-08-18.
 */
public class ValidatorScanner extends ClassPathBeanDefinitionScanner {

    private Class<?> markerInterface;

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public ValidatorScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }


    public void registerFilters() {
        if (this.markerInterface != null) {
            this.addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
        }
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(String.valueOf(definition.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Object o = BeanUtils.instantiate(clazz);
            BindingValidator bindingValidator = (BindingValidator) o;
            ValidatorCache.add(bindingValidator);
        }
        return beanDefinitions;
    }

}
