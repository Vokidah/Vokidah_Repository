package org.khadikov.projectname.annotations;
import java.lang.annotation.*;
/**
 * @author Eugene Goncharov
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Restful {
    String value();
}
