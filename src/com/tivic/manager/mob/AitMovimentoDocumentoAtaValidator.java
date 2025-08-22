package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.validation.Validator;

public class AitMovimentoDocumentoAtaValidator implements Validator<AitMovimentoDocumentoDTO> {

	private AitMovimentoDocumentoDTO _dto = null;
	
	@Override
	public Optional<String> validate(AitMovimentoDocumentoDTO object) {
		this._dto = object;
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
	
	@SuppressWarnings("unused")
	private Optional<String> validateAta() {
		int tpDocumentoAta = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_ATA", 0);
		
		if(tpDocumentoAta == 0)
			return Optional.of("Parâmetro de ATA não preenchido, não é possível validar.");
		
		return Optional.empty();
	}
	
}
