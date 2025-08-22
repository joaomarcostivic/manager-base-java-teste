package com.tivic.manager.crt;

public class Recursos {
	public static final int ADMINISTRADOR = 0;
	public static final int OPERADOR = 0;
	public static final int USUARIO_CONSULTA = 0;
	public static final int GERENTE = 0;
	public static final int ATENDENTE = 0;
	
	public static final String[] nomeEstado = {
		"Acre", "Alagoas", "Amazonas", "Amapá", "Bahia", "Ceará", "Distritro Federal", "Espírito Santo",
		"Goiás", "Maranhão", "Minas Gerais", "Mato Grosso", "Mato Grosso do Sul", "Pará", "Paraíba",
		"Pernambuco", "Piauí", "Paraná", "Rio de Janeiro", "Rio Grande do Norte", "Rio Grande do Sul",
		"Rondônia", "Roraima", "Santa Catarina", "São Paulo", "Sergipe", "Tocantins"};

	public static final String[] siglaEstado = {"AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES",
		"GO", "MA", "MG", "MT", "MS", "PA", "PB","PE", "PI", "PR", "RJ", "RN", "RS","RO", "RR",
		"SC", "SP", "SE", "TO"};

	public static final String[] siglaOrgaoEmissor = {"SSP","CRM","OAB","CREA"};

	public static final String[] tipoCategoriaParametro = {"Geral"};

	public static final String[] estadoCivil = {"Solteiro(a)", "Casado(a)", "Separado(a)",
												"Divorciado(a)", "Viúvo(a)"};

	public static final String[] tipoCalculo = {"Perc. s/ o emprestado",
												"Perc. s/ o spread",
												"Fixo por operação",
												"Perc. da TAC",
												"Perc. de outra comissão",
												"TAC menos um valor fixo",
												"Perc. s/ Taxa,Plano e Valor"};
	public static final String[] tipoComissao = {"Empresa",
												 "Atendente",
												 "Agente",
												 "Subagente"};

	public static final String[] tipoContaBancaria = {"001-C. Corrente (P.F.)",
													  "002-C. Simples (P.F.)",
													  "003-C. Corrente (P.J.)",
	                                                  "013-Poupança (P.F.)",
	                                                  "022-Poupança (P.J.)",
	                                                  "023-Conta Caixa Aqui",
	                                                  "032-Poupança (P.F.)",
	                                                  "034-Poupança (P.J.)"};

	public static final String[] situacaoVinculo = {"Inativo", "Ativo", "Bloqueado", "Suspenso"};
	public static final String[] situacaoContrato = {"Pendente", "Impresso", "Recebido"};

	public static final String[] opcoesLembrete = {"0 minutos",
												   "5 minutos",
												   "10 minutos",
												   "15 minutos",
												   "1 hora",
												   "2 horas",
												   "3 horas",
												   "4 horas",
												   "5 horas",
												   "6 horas",
												   "7 horas",
												   "8 horas",
												   "9 horas",
												   "10 horas",
												   "11 horas",
												   "0,5 dia",
												   "18 horas",
												   "1 dia",
												   "2 dias",
												   "3 dias",
												   "4 dias",
												   "1 semana",
												   "2 semanas"};

	public static final String[] unidadesTempo = {"minuto",
												  "hora",
												  "dia",
												  "semana"};

	public static final String[] diasSemana = {"Domingo",
											   "Segunda-feira",
											   "Terça-feira",
											   "Quarta-feira",
											   "Quinta-feira",
											   "Sexta-feira",
											   "Sábado"
	};

	public static final String[] tipoUsuario = {"Administrador", "Gerente", "Atendente", "Usuário Consulta"};

	public static final String[] tipoParcelamento = {"Por Pagamento", "Semanalmente", "Mensalmente"};

	public static final String[] tiposFeriado = {"Municipal",
												 "Estadual",
												 "Federal",
												 "Carnaval",
												 "Sexta-Feira Santa",
												 "Páscoa",
												 "Corpus Christi"};

	public static final String[] meses = {"Janeiro",
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

	public static final String[] situacaoOcorreciaAgendamento = {"A confirmar",
																 "Confirmado",
																 "Descartado",
																 "Adiado"};

	public static final int[] numeroDiasMeses = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	public static final String[] tiposPrioridade = {"Normal", "Urgente", "Urgentíssimo"};

	// Metas e Premiações
	public static String[] tipoApresentacaoParametro = {"Singleline Text", "Multiline Text",
														"Check Box", "Combo Box",
														"List Box","List Box (Seleção Multipla)",
														"Radio Box", "Filter", "Dual Text"};

	public static String[] tipoDadosParametro        = {"Numérico", "String", "Data", "Lógico", "Imagem"};

	public static final String[] situacaoContaPagar = {"A Pagar","Paga","Cancelada"};
	public static final String[] situacaoAdiantamento = {"Solicitado", "Autorizado", "Pago", "Descontado", "Negado"};
	public static final String[] situacaoNota = {"Em Lançamento", "Concluída", "Conferida"};
}
