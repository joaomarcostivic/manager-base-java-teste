package com.tivic.manager.ptc.tipodocumento.construcaotipodocumento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.TipoDocumentoStatus;

public class ConstrucaoDefesaPrevia implements IConstrucaoTipoDocumento{

	@Override
	public TipoDocumentoStatus montar() {
		TipoDocumentoStatus tipoDocumentos;
		List<Integer> listaDeStatus = new ArrayList<Integer>();
		listaDeStatus.add(TipoStatusEnum.DEFESA_DEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.DEFESA_INDEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.DEFESA_PREVIA.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey());
		
		tipoDocumentos = new TipoDocumentoStatus(TipoDocumentoProtocoloEnum.DEFESA_PREVIA.getKey(), 
				"Entrada de Defesa Prévia",
				listaDeStatus);
		return tipoDocumentos;
	}

}
