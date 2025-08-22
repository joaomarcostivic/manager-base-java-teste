package com.tivic.manager.wsdl.detran.mg;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.validation.ValidationException;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoStatusEnum;

public class TipoDocumentoMap {
	
	private HashMap<Integer, String> idTipoDocumentoMap;
	
	public TipoDocumentoMap() {
		this.idTipoDocumentoMap = new LinkedHashMap<Integer, String>();
		initIdTipoDocumentoMap();
	}
	
	private void initIdTipoDocumentoMap() {
		this.idTipoDocumentoMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.DEFESA_PREVIA.getKey().toString());
		this.idTipoDocumentoMap.put(TipoStatusEnum.RECURSO_JARI.getKey(), TipoStatusEnum.RECURSO_JARI.getKey().toString());
		this.idTipoDocumentoMap.put(TipoStatusEnum.RECURSO_CETRAN.getKey(), TipoStatusEnum.RECURSO_CETRAN.getKey().toString());
		this.idTipoDocumentoMap.put(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey(), TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey().toString());
	}

	public String getIdByTpStatus(int tpStatus) {
		switch(tpStatus) {
			case AitMovimentoServices.DEFESA_PREVIA:
			case AitMovimentoServices.DEFESA_DEFERIDA:
			case AitMovimentoServices.DEFESA_INDEFERIDA:
				return this.idTipoDocumentoMap.get(TipoStatusEnum.DEFESA_PREVIA.getKey());
				
			case AitMovimentoServices.RECURSO_JARI:
			case AitMovimentoServices.JARI_COM_PROVIMENTO:
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
				return this.idTipoDocumentoMap.get(TipoStatusEnum.RECURSO_JARI.getKey());
			
			case AitMovimentoServices.RECURSO_CETRAN:
			case AitMovimentoServices.CETRAN_DEFERIDO:
			case AitMovimentoServices.CETRAN_INDEFERIDO:
				return this.idTipoDocumentoMap.get(TipoStatusEnum.RECURSO_CETRAN.getKey());
				
			case AitMovimentoServices.APRESENTACAO_CONDUTOR:
				return this.idTipoDocumentoMap.get(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
			default: throw new ValidationException("Nenhum id encontrado para este status.");
		}
	}
}
