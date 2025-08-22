package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.CategoriaCnhValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.DataValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.FiciExistsValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.ModeloCnhValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.NaiValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.NicValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.NomeCondutorValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.NumeroCnhValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.ResponsabilidadeValidator;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations.UfCnhValidator;
import com.tivic.sol.connection.CustomConnection;

public class ApresentacaoCondutorValidatorBuilder {
		private List<IApresentacaoCondutorValidator> validators;
		
		public ApresentacaoCondutorValidatorBuilder() throws Exception {
			this.validators = new ArrayList<IApresentacaoCondutorValidator>();
			this.validators.add(new FiciExistsValidator());
			this.validators.add(new CategoriaCnhValidator());
			this.validators.add(new UfCnhValidator());
			this.validators.add(new NaiValidator());
			this.validators.add(new DataValidator());
			this.validators.add(new ResponsabilidadeValidator());
			this.validators.add(new NicValidator());
			this.validators.add(new ModeloCnhValidator());
			this.validators.add(new NumeroCnhValidator());
			this.validators.add(new NomeCondutorValidator());
		}
		
		public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection) throws Exception {
			for(IApresentacaoCondutorValidator validator: this.validators) {
				validator.validate(apresentacaoCondutor, connection);
			}
		}
}
