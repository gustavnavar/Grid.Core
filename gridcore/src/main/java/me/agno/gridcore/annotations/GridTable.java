package me.agno.gridcore.annotations;

import me.agno.gridcore.pagination.PagingType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GridTable {

    public PagingType PagingType() default PagingType.None;

    public int PageSize() default 0;

    public int PagingMaxDisplayedPages() default 0;
}
