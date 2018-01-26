package org.smart4j.framework.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 切面注解
 * @author Administrator
 *
 */
@Target(ElementType.TYPE)
public @interface Aspect {
	
	Class<? extends Annotation> value();
}
