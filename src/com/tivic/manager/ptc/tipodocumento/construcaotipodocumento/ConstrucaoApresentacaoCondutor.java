package com.tivic.manager.ptc.tipodocumento.construcaotipodocumento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.TipoDocumentoStatus;

public class ConstrucaoApresentacaoCondutor implements IConstrucaoTipoDocumento {

	@Override
	public TipoDocumentoStatus montar() {
		TipoDocumentoStatus tipoDocumentos;
		List<Integer> listaDeStatus = new ArrayList<Integer>();
		listaDeStatus.add(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_FICI.getKey());
		listaDeStatus.add(TipoStatusEnum.REABERTURA_FICI.getKey());
		
		tipoDocumentos = new TipoDocumentoStatus(TipoDocumentoProtocoloEnum.APRESENTACAO_CONDUTOR.getKey(), 
				"Apresentação de Condutor",
				listaDeStatus);
		return tipoDocumentos;
	}

}
