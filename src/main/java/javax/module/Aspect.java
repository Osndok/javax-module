package javax.module;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * At the moment, this annotation does nothing. In the near future, this will be a hint for the plugin system to
 * handle particular "plugins" in a special, almost aspect-orient nature... that one plugin can intercept, precede,
 * or follow any other plugin that it is 'aware' of (which requires a dependency link).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public
@interface Aspect
{
	Class[] before() default {};
	Class[] after() default {};
	Class[] replaces() default {};
}
