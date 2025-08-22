package com.tivic.manager.ptc.protocolosv3.publicacao.relatorios;

import java.util.List;

import com.tivic.manager.ptc.protocolosv3.publicacao.ProtocoloPublicacaoPendenteDto;

public interface IGerarRelatorioProcessos {

	public byte[] gerar(String idRecurso, int julgamento, List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) throws Exception;

}
