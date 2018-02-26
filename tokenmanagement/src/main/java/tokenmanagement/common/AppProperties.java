package tokenmanagement.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Documented
@Qualifier
@Retention(RUNTIME)
@Target({ FIELD, TYPE, METHOD, CONSTRUCTOR })
public @interface AppProperties {

	/**
	 * This value must be a property name from config file.
	 */
	@Nonbinding
	String name() default "";

	// /**
	// * This value must be a properties file in the classpath.
	// */
	// String path() default "/config/application.properties";
}
