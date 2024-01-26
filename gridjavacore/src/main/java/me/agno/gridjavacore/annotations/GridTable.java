package me.agno.gridjavacore.annotations;

import me.agno.gridjavacore.pagination.PagingType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GridTable {

    PagingType pagingType() default PagingType.NONE;

    int pageSize() default 0;

    int pagingMaxDisplayedPages() default 0;
}
