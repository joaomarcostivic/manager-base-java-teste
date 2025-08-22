package com.tivic.manager.ptc.protocolosv3.builders;

public class SituacaoDocumentoResultado {
	public boolean isIndeferido(int situacaoDocumento) throws Exception {
		int cdSituacaoIndeferida = new ParamGetValid().getParamValue("CD_SITUACAO_DOCUMENTO_INDEFERIDO");
		
		if(situacaoDocumento == cdSituacaoIndeferida)
			return true;
		return false;
	}
	
	public boolean isDeferido(int situacaoDocumento) throws Exception {
		int cdSituacaodeferida = new ParamGetValid().getParamValue("CD_SITUACAO_DOCUMENTO_DEFERIDO");
		
		if(situacaoDocumento == cdSituacaodeferida)
			return true;
		return false;

	}
	
}
