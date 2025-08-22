package com.tivic.manager.wsdl.detran.mg.recursofici;

public enum TipoParecerFici {
	INFRACAO_RESPONSABILIDADE_PROPRIETARIO(1, "Infração de responsabilidade do proprietário do veículo"),
	FICI_INTEMPESTIVO(2, "FICI intempestivo (fora do prazo legal)"),
	OUTRO_CONDUTOR_INFORMADO_NO_AUTO(3,"Outro condutor infrator informado no auto de infração"),
	CNH_PPD_INVALIDO(4, "Número da CNH (ou PPD) do condutor infrator inválido"),
	FICI_SEM_ASSINATURA(6, "FICI sem assinatura (proprietário do veículo e/ou condutor infrator)"),
	FICI_SEM_DOCUMENTOS_CONDUTOR(7, "FICI sem cópias de documentos do condutor infrator"),
	FICI_COM_DOCUMENTOS_ILEGIVEIS(9, "Cópia de documentos ilegível (Artigo 4 da Resolução 404)"),
	DATA_INFRACAO_FORA_DE_VIGENCIA_CONTRATO(10, "Data da infração fora da vigência do contrato de locação"),
	PEDIDO_IDENTIFICACAO_ACEITO(22, "Pedido de identificação Aceito");
	
	private final int tipoParecerFici;
	private final String descricaoParecer;
	
	TipoParecerFici(int tipoParecer, String descricao) {
		this.tipoParecerFici = tipoParecer;
		this.descricaoParecer = descricao;
	}

	public int getTipoParecerFici() {
		return tipoParecerFici;
	}
	
	public String getDescricaoParecer() {
		return descricaoParecer;
	}
	
	public static String valueOf(Integer key) {
		for(Integer index=0; index < TipoParecerFici.values().length; index++) {
			if(key == TipoParecerFici.values()[index].getTipoParecerFici()) {
				return TipoParecerFici.values()[index].getDescricaoParecer();
			}
		}
		return null;
	}
}
