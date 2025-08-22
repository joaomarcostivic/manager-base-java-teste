package com.tivic.manager.wsdl.detran.mg.enviorenainf;

import com.tivic.manager.mob.TipoStatusEnum;

public enum CodigoMovimentacaoRenainfEnum {
	ENVIO_INFRACAO(TipoStatusEnum.REGISTRO_INFRACAO.getKey(), "001"),
	ENVIO_NOTIFICACAO_AUTUACAO(TipoStatusEnum.NAI_ENVIADO.getKey(), "002"),
	ENVIO_CADASTRO_DEFESA_AUTUACAO(TipoStatusEnum.DEFESA_PREVIA.getKey(), "003"),
	ENVIO_CADASTRO_ADVERTENCIA_ESCRITO(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey(), "004"),
	ENVIO_RESULTADO_DEFESA_DEFERIDA(TipoStatusEnum.DEFESA_DEFERIDA.getKey(), "005"),
	ENVIO_RESULTADO_DEFESA_INDEFERIDA(TipoStatusEnum.DEFESA_INDEFERIDA.getKey(), "005"),
	ENVIO_RESULTADO_ADVERTENCIA_DEFERIDO(TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey(), "006"),
	ENVIO_RESULTADO_ADVERTENCIA_INDEFERIDO(TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey(), "006"),
	ENVIO_NOTIFICACAO_PENALIDADE(TipoStatusEnum.NIP_ENVIADA.getKey(), "007"),
	ENVIO_CADASTRO_RECURSO(TipoStatusEnum.RECURSO_JARI.getKey(), "008"),
	ENVIO_JULGAMENTO_RECURSO_PROV(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey(), "009"),
	ENVIO_JULGAMENTO_RECURSO_SEM_PROV(TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey(), "009"),
	ENVIO_PAGAMENTO_INFRACAO(TipoStatusEnum.MULTA_PAGA.getKey(), "010"),
	ENVIO_FICI(TipoStatusEnum.TRANSFERENCIA_PONTUACAO.getKey(), "011"),
	ENVIOS_MULTIPLOS_RENAINF(TipoStatusEnum.ENVIOS_MULTIPLOS_RENAINF.getKey(), "012"),
	ENVIO_CANCELAMENTO_AUTUACAO(TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey(), "013"),
	ENVIO_CANCELAMENTO_PONTUACAO(TipoStatusEnum.CANCELAMENTO_PONTUACAO.getKey(), "014"),
	ENVIO_CANCELAMENTO_MULTA(TipoStatusEnum.CANCELAMENTO_MULTA.getKey(), "014"),
	ENVIO_DADOS_CETRAN(TipoStatusEnum.RECURSO_CETRAN.getKey(), "015"),
	ENVIO_CETRAN_DEFERIDO(TipoStatusEnum.CETRAN_DEFERIDO.getKey(), "015"),
	ENVIO_CETRAN_INDEFERIDO(TipoStatusEnum.CETRAN_INDEFERIDO.getKey(), "015"),
	ENVIO_DADOS_RECURSO(TipoStatusEnum.CANCELAMENTO_MULTA.getKey(), "016"),
	ENVIO_DADOS_ADVERTENCIA(TipoStatusEnum.CANCELAMENTO_MULTA.getKey(), "017"),
	ENVIO_EFEITO_SUSPENSIVO(TipoStatusEnum.PENALIDADE_SUSPENSA.getKey(), "018");
	
	private final Integer key;
	private final String value;

	CodigoMovimentacaoRenainfEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public final Integer getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static String valueOf(Integer key) {
		for (int index = 0; index < CodigoMovimentacaoRenainfEnum.values().length; index++) {
			if (key ==  (Integer.parseInt(CodigoMovimentacaoRenainfEnum.values()[index].getKey().toString()))) {
				return CodigoMovimentacaoRenainfEnum.values()[index].getValue();
			}
		}
		return null;
	}

}
