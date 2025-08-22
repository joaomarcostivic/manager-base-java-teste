package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.GregorianCalendar;
import java.util.Optional;

import com.tivic.manager.validation.Validator;

@SuppressWarnings("unused")
public class BoatDatValidator implements Validator<Boat> {
	
	private Boat _boat = null;
	private BoatServices _boatServices = new BoatServices(); 
	
	@Override
	public Optional<String> validate(Boat object) {
		this._boat = object;
		try {
			Method[] methods = this.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if(method.getName().endsWith("validate"))
					continue;
				
				@SuppressWarnings("unchecked")
				Optional<String> op = ((Optional<String>) method.invoke(this));
				if(op.isPresent()) {
					return op;
				}
			}

			return Optional.empty();
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		}
	}

}
