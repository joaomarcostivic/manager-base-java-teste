package com.tivic.manager.mob;

import java.util.Optional;

import com.tivic.manager.validation.Validator;

public class AitNIPValidator implements Validator<Ait> {

	@Override
	public Optional<String> validate(Ait obj) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Optional<String> hasNip() {
		// procura nip do ait
		
		if(true)
			return Optional.of("Já existe NIP para o ait");
		else
			return Optional.empty();
		
	}

}
