package com.tivic.manager.mob;

import java.sql.Types;
import java.util.Optional;

import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.validation.Validator;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class AitMovimentoDocumentoExistsValidator implements Validator<AitMovimentoDocumentoDTO> {

	@Override
	public Optional<String> validate(AitMovimentoDocumentoDTO obj) {
		
		ResultSetMap rsm = DocumentoServices
					.find(new Criterios()
							.add("A.nr_documento", obj.getDocumento().getNrDocumento(), ItemComparator.EQUAL, Types.VARCHAR)
							.add("A.cd_tipo_documento", Integer.toString(obj.getDocumento().getCdTipoDocumento()), ItemComparator.EQUAL, Types.INTEGER)
							.add("A.cd_setor", Integer.toString(obj.getDocumento().getCdSetor()), ItemComparator.EQUAL, Types.INTEGER));
		
		if(rsm.next()) {
			return Optional.of("Documento já existe.");
		}
		
		return Optional.empty();
	}
}
