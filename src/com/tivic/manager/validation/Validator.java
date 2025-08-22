package com.tivic.manager.validation;

import java.util.Optional;

@FunctionalInterface
public interface Validator<T> {
	
	Optional<String> validate(final T obj);

}
