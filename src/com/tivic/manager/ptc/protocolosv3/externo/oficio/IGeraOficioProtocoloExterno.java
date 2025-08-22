package com.tivic.manager.ptc.protocolosv3.externo.oficio;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;

import java.util.List;

public interface IGeraOficioProtocoloExterno {

	public LoteImpressao gerarOficio(List<ProtocoloExternoDTO> protocoloExternoDTO) throws Exception;	
}
