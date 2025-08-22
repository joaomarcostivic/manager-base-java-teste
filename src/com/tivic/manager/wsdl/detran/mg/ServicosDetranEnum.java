package com.tivic.manager.wsdl.detran.mg;

public enum ServicosDetranEnum {
	BAIXA_SUSPENSAO("incluirBaixaSuspensaoRetornoInfracao"),
	INCLUIR_AUTO_INFRACAO("incluirAutoInfracao"),
	INCLUIR_DEFESA_AUTUACAO("incluirDefesaAutuacao"),
	INCLUIR_BAIXA_SUSPENSAO_RETORNO_INFRACAO("incluirBaixaSuspensaoRetornoInfracao"),
	SINCRONIZAR_BASE_NACIONAL_COM_ESTADUAL("sincronizarBaseNacionalComBaseEstadual"),
	INCLUIR_NOTIFICACAO_PUBLICACAO_PRAZO("incluirNotificacaoPublicacaoPrazo"),
	INCLUIR_IDENTIFICACAO_CONDUTOR_INFRATOR("incluirIdentificacaoCondutorInfrator"),
	INCLUIR_RECURSO_INFRACAO("incluirRecursoInfracao"),
	CONSULTAR_INFRACAO_BASE_ESTADUAL("consultarAutoInfracaoBaseEstadual"),
	CONSULTAR_INFRACAO_BASE_NACIONAL("consultarAutoInfracaoBaseNacional"),
	CONSULTAR_MOVIMENTACOES_INFRACAO("consultarMovimentacoesInfracao"),
	CONSULTAR_VEICULO_PLACA("consultarVeiculoPorPlaca"),
	CONSULTAR_DEFESA_RECURSO_INFRACAO("consultarDefesaRecursoInfracao"),
	CONSULTAR_PONTUACAO_DADOS_CONDUTOR("consultarPontuacaoDadosCondutor"),
	ALTERAR_DATA_RECURSO("alterarDataLimiteRecurso"),
	CONSULTAR_INFRACOES_POR_CNH_DATA_INFRACAO("consultarInfracoesPorCnhDataInfracao"),
	INCLUIR_MULTA_NIC("incluirMultaCondutorNaoIdentificado"),
	CONSULTAR_POSSUIDOR_VEICULO_PLACA("consultarPossuidoresVeiculoPorPlaca");
	
	private final String value;
	
	ServicosDetranEnum(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
