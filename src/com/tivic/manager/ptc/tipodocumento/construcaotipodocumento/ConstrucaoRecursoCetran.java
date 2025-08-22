package com.tivic.manager.ptc.tipodocumento.construcaotipodocumento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.TipoDocumentoStatus;

public class ConstrucaoRecursoCetran implements IConstrucaoTipoDocumento{

	@Override
	public TipoDocumentoStatus montar() {
		TipoDocumentoStatus tipoDocumentos;
		List<Integer> listaDeStatus = new ArrayList<Integer>();
		listaDeStatus.add(TipoStatusEnum.CETRAN_DEFERIDO.getKey());
		listaDeStatus.add(TipoStatusEnum.CETRAN_INDEFERIDO.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_CETRAN_COM_PROVIMENTO.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_CETRAN_COM_PROVIMENTO.getKey());
		listaDeStatus.add(TipoStatusEnum.CANCELAMENTO_RECURSO_CETRAN.getKey());
		listaDeStatus.add(TipoStatusEnum.RECURSO_CETRAN.getKey());
		
		tipoDocumentos = new TipoDocumentoStatus(TipoDocumentoProtocoloEnum.RECURSO_CETRAN.getKey(), 
				"Entrada Recurso CETRAN",
				listaDeStatus);
		return tipoDocumentos;
	}
}
