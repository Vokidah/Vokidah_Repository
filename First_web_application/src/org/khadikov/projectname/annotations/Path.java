package org.khadikov.projectname.annotations;
import java.lang.annotation.*;
/**
 * @author Eugene Goncharov
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
    String value();
}
