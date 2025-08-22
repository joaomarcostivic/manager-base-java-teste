package com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem;

public enum ArquivoPrevisaoPostagemEnum {

	FIXO_NOME_ARQUIVO(1, "Número fixo no nome do arquivo"),
	TP_REGISTRO_HEADER(8, "Tipo de registro do arquivo de previsão postagem cabeçalho"),
	TP_REGISTRO_DADOS(9, "Tipo de registro do arquivo de previsão postagem cabeçalho"),
	NR_SEQUENCIAL_HEADER(1, "Número sequencial registro cabeçalho"),
	CODIGO_OPERACAO(1101, "Inclusão de dados da Encomenda."),
	NR_INICIAL_SEQUENCIAL_DADOS(2, "Número inicial da sequencia de dados do arquivo a iniciar com 2");
	
	private final Integer key;
	private final String value;
	
	ArquivoPrevisaoPostagemEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public static String valueOf(Integer key) {
		for(Integer index=0; index < ArquivoPrevisaoPostagemEnum.values().length; index++) {
			if(key == ArquivoPrevisaoPostagemEnum.values()[index].getKey()) {
				return ArquivoPrevisaoPostagemEnum.values()[index].getValue();
			}
		}
		return null;
	}
	
}
