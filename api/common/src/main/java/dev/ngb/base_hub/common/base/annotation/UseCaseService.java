package dev.ngb.base_hub.common.base.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AppComponent
public @interface UseCaseService {
}
