package com.tivic.manager.mob.restituicao;

import java.util.List;

public interface IGerarRelatorioRestituicao {
	public byte[] gerar(List<RestituicaoDTO> aitsList) throws Exception;
}
