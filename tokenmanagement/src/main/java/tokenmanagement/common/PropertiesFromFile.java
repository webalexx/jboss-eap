package tokenmanagement.common;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;


@Qualifier
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD, CONSTRUCTOR })
public @interface PropertiesFromFile {

	/**
     * This value must be a properties file in the classpath.
     */
    @Nonbinding
    String value() default "/config/application.properties";
    @Nonbinding 
    boolean required() default true;
    
}
