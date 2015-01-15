package com.easysocial.types.packed;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)

public @interface SQLType {
	String LONG_TEXT = "varchar(65536)";
	String DEFAULT_TYPE = "varchar(100)";

	String type();
}
