package com.tivic.manager.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Validators<T> extends ArrayList<Validator<T>> {
	private static final long serialVersionUID = 3485601391391291657L;
	
	private final T object;	
	
	public Validators(T object) {
		this.object = object;
	}

	public Optional<String> validateAll() {
		for (Validator<T> v : this) {
			Optional<String> result = v.validate(object);
			if(result.isPresent())
				return result;
		}
		return Optional.empty();
	}
	
	public List<Optional<String>> validationResults() {
		List<Optional<String>> results = new ArrayList<Optional<String>>();
		for (Validator<T> v : this) {
			results.add(v.validate(object));
		}
		return results;
	}

	public Validators<T> put(Validator<T> v) {
		this.add(v);
		return this;
	}
	
}
