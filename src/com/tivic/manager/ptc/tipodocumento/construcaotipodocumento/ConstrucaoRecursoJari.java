package com.tivic.manager.ptc.tipodocumento.construcaotipodocumento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.TipoDocumentoStatus;

public class ConstrucaoRecursoJari implements IConstrucaoTipoDocumento{

	@Override
	public TipoDocumentoStatus montar() {
		TipoDocumentoStatus tipoDocumentos;
		List<Integer> listaDeStatus = new ArrayList<Integer>();
		listaDeStatus.add(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey());
		listaDeStatus.add(TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey());
		listaDeStatus.add(TipoStatusEnum.RECURSO_JARI.getKey());
		listaDeStatus.add(TipoStatusEnum.REABERTURA_JARI.getKey());
		listaDeStatus.add(TipoStatusEnum.NOVO_PRAZO_JARI.getKey());
		
		tipoDocumentos = new TipoDocumentoStatus(TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey(),
				"Entrada Recurso Jari",
				listaDeStatus);
		return tipoDocumentos;
	}
}
