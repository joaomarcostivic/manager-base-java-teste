package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.validation.Validator;

@SuppressWarnings("unused")
public class OrgaoServicoValidator implements Validator<OrgaoServico>  {
	
	private OrgaoServico object = null;
	private OrgaoServicoServices _service = new OrgaoServicoServices();
	
	@Override
	public Optional<String> validate(OrgaoServico object) {	
		this.object = object;
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
	
	private Optional<String> validateChave() {
		if(ParametroServices.getValorOfParametroAsInteger("MOB_CD_ORGAO_SERVICO_ID", 0) <= 0)
			return Optional.of("Parametro de ID do Serviço do Órgão não preenchido, favor verificar");
		
		return Optional.empty();
	}

}
