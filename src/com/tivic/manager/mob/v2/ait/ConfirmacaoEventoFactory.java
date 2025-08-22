package com.tivic.manager.mob.v2.ait;

public class ConfirmacaoEventoFactory {

	public static ConfirmacaoEvento factory(InformacoesAitDTO informacoesAitDTO) {
		return new ConfirmacaoEvento(informacoesAitDTO.getCdUsuarioConfirmacao(), informacoesAitDTO.getNrPlaca());
	}
	
}
