package me.agno.gridjavacore.annotations;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a class as a grid metadata type.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GridMetadataType {

    /**
     * Retrieves the metadata type associated with the grid.
     *
     * @return the metadata type associated with the grid
     */
    @NonNull
    Class<?> metadataType();
}
