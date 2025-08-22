package com.tivic.manager.ptc.tipodocumento.construcaotipodocumento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.TipoDocumentoStatus;

public class ConstrucaoDefesaAdvertencia implements IConstrucaoTipoDocumento{

	@Override
	public TipoDocumentoStatus montar() {
		TipoDocumentoStatus tipoDocumentos;
		List<Integer> listaDeStatus = new ArrayList<Integer>();
		listaDeStatus.add(TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
		listaDeStatus.add(TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey());
		
		tipoDocumentos = new TipoDocumentoStatus(TipoDocumentoProtocoloEnum.DEFESA_PREVIA_ADVERTENCIA.getKey(), 
				"Advertência por Escrito",
				listaDeStatus);
		return tipoDocumentos;
	}
}
