package com.tivic.manager.util;

public class Recursos {
	/**
	 * @category SICOE
	 */
	public static final String[] situacaoVinculo = {"Inativo", "Ativo", "Bloqueado", "Suspenso"};
	public static final String[] situacaoContrato = {"Pendente", "Impresso", "Recebido"};
	public static final String[] tipoCalculo = {"Perc. s/ o emprestado",
		"Perc. s/ o spread",
		"Fixo por operação",
		"Perc. da TAC",
		"Perc. de outra comissão",
		"TAC menos um valor fixo",
		"Perc. s/ Taxa, Plano e Valor",
		"Informada na Planilha"};
	public static final String[] tipoComissao = {"Empresa","Atendente","Agente","Subagente"};

	/************************ CADASTRO GERAL *************************/
	
	public static String[] diasSemana = {"Domingo",
		"Segunda-feira",
		"Terça-feira",
		"Quarta-feira",
		"Quinta-feira",
		"Sexta-feira",
		"Sabádo"};

	public static String[] meses = {"Janeiro",
		"Fevereiro",
		"Março",
		"Abril",
		"Maio",
		"Junho",
		"Julho",
		"Agosto",
		"Setembro",
		"Outubro",
		"Novembro",
		"Dezembro"};
	
	public static String[] tipoApresentacaoParametro = {"Singleline Text",
														"Multiline Text",
														"Check Box",
														"Combo Box",
														"List Box",
														"List Box (Seleção Multipla)",
														"Radio Box",
														"Filter",
														"Dual Text"};

	public static String[] tipoDadosParametro        = {"Numérico",
														"String",
														"Data",
														"Lógico",
														"Imagem"};

	public static String[] siglasEstados  			 = {"AC", "AL", "AM", "AP", "BA", "CE",
										    			"DF", "ES", "GO", "MA", "MG", "MS",
										    			"MT", "PA", "PB", "PE", "PI", "PR",
										    			"RJ", "RN", "RO", "RR", "RS", "SC",
										    			"SE", "SP", "TO"};

	public static String[] estados  				 = {"ACRE", "ALAGOAS", "AMAZONAS", "AMAPÁ",
														"BAHIA", "CEARÁ", "DISTRITO FEDERAL",
														"ESPÍRITO SANTO", "GOIÁS", "MARANHÃO",
														"MINAS GERAIS", "MATO GROSSO DO SUL",
														"MATO GROSSO", "PARÁ", "PARAÍBA",
														"PERNAMBUCO", "PIAUÍ", "PARANÁ",
														"RIO DE JANEIRO", "RIO GRANDE DO NORTE",
														"RONDÔNIA", "RORAIMA", "RIO GRANDE DO SUL",
														"SANTA CATARINA", "SERGIPE",
														"SÃO PAULO", "TOCANTIS"};

	public static String[][] siglasNomesEstados  	 = {{"AC", "ACRE"}, {"AL", "ALAGOAS"}, {"AM", "AMAZONAS"}, {"AP", "AMAPÁ"},
														{"BA", "BAHIA"}, {"CE", "CEARÁ"}, {"DF", "DISTRITO FEDERAL"},
														{"ES", "ESPÍRITO SANTO"}, {"GO", "GOIÁS"}, {"MA", "MARANHÃO"},
														{"MG", "MINAS GERAIS"}, {"MS", "MATO GROSSO DO SUL"},
														{"MT", "MATO GROSSO"}, {"PA", "PARÁ"}, {"PB", "PARAÍBA"},
														{"PE", "PERNAMBUCO"}, {"PI", "PIAUÍ"}, {"PR", "PARANÁ"},
														{"RJ", "RIO DE JANEIRO"}, {"RN", "RIO GRANDE DO NORTE"},
														{"RO", "RONDÔNIA"}, {"RR", "RORAIMA"}, {"RS", "RIO GRANDE DO SUL"},
														{"ST", "SANTA CATARINA"}, {"SE", "SERGIPE"},
														{"SP", "SÃO PAULO"}, {"TO", "TOCANTIS"}};

	public static String[] siglaMeses = {"Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set", "Out", "Nov", "Dez"};
	
	public static final String[] estadoCivil = {"Solteiro(a)", "Casado(a)", "Separado(a)",
		"Divorciado(a)", "Viúvo(a)"};

	public static final String[] situacaoOcorrencia = {"Pendente", "Concluído", "Cancelado"};

	public static final String[] tipoProdutoServico = {"Produto", "Serviço"};
	
	public static final String[] tipoDado = {"String",
											 "Inteiro",
											 "Float",
											 "Data",
											 "Sim/Não",
											 "Memo",
											 "Opções",
											 "Calculado",
											 "Cadastro de Pessoas"};
	
	public static final String[] tipoRestricaoPessoa = {"Nenhuma",
											 "Somente Pessoas Jurídicas",
											 "Somente Pessoas Físicas"};

	/************************ ADMINISTRATIVO *************************/
	
	public static final String[] tipoContaBancaria = {"001-C. Corrente (P.F.)",
													  "002-C. Simples (P.F.)",
													  "003-C. Corrente (P.J.)",
	                                                  "013-Poupança (P.F.)",
	                                                  "022-Poupança (P.J.)",
	                                                  "023-Conta Caixa Aqui",
	                                                  "032-Poupança (P.F.)",
	                                                  "034-Poupança (P.J.)"};
	
	/************************ PATRIMONIAL *************************/

	public static final String[] tipoManutencao = {"Corretiva", "Preventiva", "Preditiva", "Produtiva Total"};

	public static final int[] diasMeses = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
}
