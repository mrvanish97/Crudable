package io.github.uonagent.crudable.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Column {

  String name() default "";

  boolean unique() default false;

}
