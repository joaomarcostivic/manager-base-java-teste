package com.tivic.manager.wsdl.detran.mg;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;

public class TabelasAuxiliaresMG implements TabelasAuxiliares{

	public static final int TP_DOCUMENTO_CPF 				= 1;
	public static final int TP_DOCUMENTO_CNPJ 				= 2;
	public static final int TP_DOCUMENTO_RG				= 3;
	public static final int TP_DOCUMENTO_DOC_ESTRANGEIRO	= 4;
	public static final int TP_DOCUMENTO_OUTROS			= 5;
	public static final int TP_DOCUMENTO_NAO_APRESENTOU	= 9;
	
	public HashMap<Integer, Integer> tiposDocumento;
	
	public static final int TP_CNH_PGU 					= 1;
	public static final int TP_CNH_RENACH 					= 2;
	public static final int TP_CNH_HABILITACAO_ESTRANGEIRA	= 3;
	public static final int TP_CNH_NAO_HABILITADO			= 4;
	
	public HashMap<Integer, Integer> tiposCnh;
	
	public static final int LG_ASSINADO			= 1;
	public static final int LG_NAO_ASSINADO		= 2;
	public static final int LG_RECUSOU_ASSINAR	= 3;
	
	public HashMap<Integer, Integer> lgAssinatura;	

	public static final int MOV_DEFESA_PREVIA		= 36;
	public static final int MOV_DEFESA_DEFERIDA	= 37;
	public static final int MOV_DEFESA_INDEFERIDA	= 38;
	public static final int MOV_FIM_PRAZO_DEFESA	= 39;
	
	public HashMap<Integer, Integer> movimentoDefesa;
	
	public static final int MOV_ADVERTENCIA_ENTRADA		= 104;
	public static final int MOV_ADVERTENCIA_DEFERIDA  	= 105;
	public static final int MOV_ADVERTENCIA_INDEFERIDA	= 106;
	public static final int MOV_FIM_PRAZO_ADVERTENCIA	= 39;
	
	public HashMap<Integer, Integer> movimentoAdvertencia;
	
	public static final int CNH_ARGENTINA		= 10;
	public static final int CNH_BOLIVIA 		= 11;
	public static final int CNH_GUIANA 		= 20;
	public static final int CNH_CHILE 			= 30;
	public static final int CNH_VENEZUELA 		= 40;
	public static final int CNH_PARAGUAI 		= 60;
	public static final int CNH_URUGUAI 		= 80;
	public static final int CNH_MEXICO			= 90;
	public static final int CNH_EUA				= 91;
	public static final int CNH_CANADA			= 92;
	public static final int CNH_OUTROS			= 99;
	
	public HashMap<Integer, Integer> paisCnh;
	
	
	public static final int BAIXA_QUITACAO_BANCARIA 							= 2;
	public static final int BAIXA_ERRO_LANCAMENTO_AUTO 							= 6;
	public static final int BAIXA_SENTENCA_JUDICIAL 							= 8;
	public static final int BAIXA_SOLICITACAO_AUTORIDADE_TRANSITO 				= 9;
	public static final int BAIXA_RETORNO_INFRACAO_BAIXADA 						= 15;
	public static final int BAIXA_EFEITO_SUSPENSIVO_JUDICIAL_LICENCIAMENTO 		= 16;
	public static final int BAIXA_EFEITO_SUSPENSIVO_JUDICIAL_TRANSFERENCIA 		= 17;
	public static final int BAIXA_EFEITO_SUSPENSIVO_ADMINISTRATIVO 				= 18;
	public static final int BAIXA_ENCERRAMENTO_EFEITO_SUSPENSIVO 				= 19;
	public static final int BAIXA_CANCELAMENTO_EFEITO_SUSPENSIVO 				= 20;
	public static final int BAIXA_EFEITO_SUSPENSIVO_ADMINISTRATIVO_TRANSFERENCIA= 27;
	public static final int BAIXA_CONVERSAO_DIVIDA_ATIVA 						= 28;
	public static final int BAIXA_DESVINCULADA_LEILAO 							= 29;
	public static final int BAIXA_CANCELAMENTO_PAGAMENTO 						= 46;
	public static final int BAIXA_EFEITO_SUSPENSIVO_PARCELAMENTO 				= 47;
	public static final int BAIXA_CANCELAMENTO_EFEITO_SUSPENSIVO_PARCELAMENTO	= 48;
	public static final int BAIXA_CANCELA_NIC 									= 49;
	public static final int BAIXA_INFORMA_RESTITUICAO 							= 91;
	public static final int BAIXA_PRESCRICAO 									= 99;
	public static final int BAIXA_MULTA_SUSPENSA 								= 123;
	public static final int BAIXA_CANCELAMENTO_NIP 								= 127;
	public static final int CANCELAMENTO_DIVIDA_ATIVA 							= 143;
	public static final int BAIXA_EXCLUSAO_AIT_NAO_NOTIFICADO 					= 999;
	
	public HashMap<Integer, Integer> movimentoBaixa;
	
	public static final int PRAZO_REGISTRO_INFRACAO								= 26;
	public static final int PRAZO_ENVIO_NAI										= 30;
	public static final int PRAZO_ENVIO_NIP										= 180;
	
	public HashMap<Integer, Integer> prazosMovimentos;
	public HashMap<Integer, Integer> statusCancelamentos;
			
		
	
	public TabelasAuxiliaresMG() {
		initTipoDocumento();
		initTipoCnh();
		initAssinatura();
		initMovimentoDefesa();
		initPaisCnh();
		initMovimentoBaixa();
		initMovimentoAdvertencia();
		initPrazosMovimentos();
		initStatusCancelamentos();
		initDadosCorreios();
	}
	
	private void initMovimentoBaixa() {
		movimentoBaixa = new LinkedHashMap<Integer, Integer>();
		movimentoBaixa.put(AitMovimentoServices.MULTA_PAGA, BAIXA_QUITACAO_BANCARIA);
		movimentoBaixa.put(AitMovimentoServices.CANCELAMENTO_NIP, BAIXA_CANCELAMENTO_NIP);		
		movimentoBaixa.put(AitMovimentoServices.PENALIDADE_SUSPENSA, BAIXA_EFEITO_SUSPENSIVO_ADMINISTRATIVO);
		movimentoBaixa.put(AitMovimentoServices.PENALIDADE_REATIVADA, BAIXA_ENCERRAMENTO_EFEITO_SUSPENSIVO);
		movimentoBaixa.put(AitMovimentoServices.SUSPENSAO_DIVIDA_ATIVA, BAIXA_MULTA_SUSPENSA);
		movimentoBaixa.put(AitMovimentoServices.BAIXA_DESVINCULADA_LEILAO, BAIXA_DESVINCULADA_LEILAO);
		movimentoBaixa.put(AitMovimentoServices.CANCELAMENTO_DIVIDA_ATIVA, CANCELAMENTO_DIVIDA_ATIVA);
	}
	
	private void initTipoDocumento(){
		tiposDocumento = new LinkedHashMap<Integer, Integer>();
		tiposDocumento.put(AitServices.TP_DOCUMENTO_CPF, TP_DOCUMENTO_CPF);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_CNPJ, TP_DOCUMENTO_CNPJ);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_RG, TP_DOCUMENTO_RG);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_DOC_ESTRANGEIRO, TP_DOCUMENTO_DOC_ESTRANGEIRO);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_OUTROS, TP_DOCUMENTO_OUTROS);
		tiposDocumento.put(AitServices.TP_DOCUMENTO_NAO_APRESENTOU, TP_DOCUMENTO_NAO_APRESENTOU);
	}
	
	private void initTipoCnh(){
		tiposCnh = new LinkedHashMap<Integer, Integer>();
		tiposCnh.put(AitServices.TP_CNH_NENHUMA, TP_CNH_NAO_HABILITADO);
		tiposCnh.put(0, null);
		tiposCnh.put(AitServices.TP_CNH_ANTIGA_MG, TP_CNH_RENACH);
		tiposCnh.put(AitServices.TP_CNH_NOVA_MG, TP_CNH_RENACH);
		tiposCnh.put(AitServices.TP_CNH_HABILITACAO_ESTRANGEIRA_MG, TP_CNH_HABILITACAO_ESTRANGEIRA);
		tiposCnh.put(AitServices.TP_CNH_NAO_HABILITADO_MG, TP_CNH_NAO_HABILITADO);
	}
	
	private void initAssinatura(){
		lgAssinatura = new LinkedHashMap<Integer, Integer>();
		lgAssinatura.put(AitServices.LG_NAO_ASSINADO_ANTIGO, LG_NAO_ASSINADO);
		lgAssinatura.put(AitServices.LG_ASSINADO, LG_ASSINADO);
		lgAssinatura.put(AitServices.LG_NAO_ASSINADO, LG_NAO_ASSINADO);
		lgAssinatura.put(AitServices.LG_RECUSOU_ASSINAR, LG_RECUSOU_ASSINAR);
	}

	private void initMovimentoDefesa(){
		movimentoDefesa = new LinkedHashMap<Integer, Integer>();
		movimentoDefesa.put(AitMovimentoServices.DEFESA_PREVIA, MOV_DEFESA_PREVIA);
		movimentoDefesa.put(AitMovimentoServices.DEFESA_DEFERIDA, MOV_DEFESA_DEFERIDA);
		movimentoDefesa.put(AitMovimentoServices.DEFESA_INDEFERIDA, MOV_DEFESA_INDEFERIDA);
		movimentoDefesa.put(AitMovimentoServices.FIM_PRAZO_DEFESA, MOV_FIM_PRAZO_DEFESA);
	}
	
	private void initMovimentoAdvertencia(){
		movimentoAdvertencia = new LinkedHashMap<Integer, Integer>();
		movimentoAdvertencia.put(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA, MOV_ADVERTENCIA_ENTRADA);
		movimentoAdvertencia.put(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA, MOV_ADVERTENCIA_DEFERIDA);
		movimentoAdvertencia.put(AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA, MOV_ADVERTENCIA_INDEFERIDA);
		movimentoAdvertencia.put(AitMovimentoServices.FIM_PRAZO_DEFESA, MOV_FIM_PRAZO_ADVERTENCIA);
	}
	
	private void initPaisCnh() {
		paisCnh = new LinkedHashMap<Integer, Integer>();
		
		paisCnh.put(AitServices.CNH_ARGENTINA, CNH_ARGENTINA);
		paisCnh.put(AitServices.CNH_BOLIVIA, CNH_BOLIVIA);
		paisCnh.put(AitServices.CNH_GUIANA, CNH_GUIANA);
		paisCnh.put(AitServices.CNH_CHILE, CNH_CHILE);
		paisCnh.put(AitServices.CNH_VENEZUELA, CNH_VENEZUELA);
		paisCnh.put(AitServices.CNH_PARAGUAI, CNH_PARAGUAI);
		paisCnh.put(AitServices.CNH_URUGUAI, CNH_URUGUAI);
		paisCnh.put(AitServices.CNH_MEXICO, CNH_MEXICO);
		paisCnh.put(AitServices.CNH_EUA, CNH_EUA);
		paisCnh.put(AitServices.CNH_CANADA, CNH_CANADA);
		paisCnh.put(AitServices.CNH_OUTROS, CNH_OUTROS);		
	}
	
	private void initPrazosMovimentos() {
		prazosMovimentos = new LinkedHashMap<Integer, Integer>();
		
		prazosMovimentos.put(AitMovimentoServices.REGISTRO_INFRACAO, PRAZO_REGISTRO_INFRACAO);
		prazosMovimentos.put(AitMovimentoServices.NAI_ENVIADO, PRAZO_ENVIO_NAI);
		prazosMovimentos.put(AitMovimentoServices.NIP_ENVIADA, PRAZO_ENVIO_NIP);
	}

	private void initStatusCancelamentos() {
		statusCancelamentos = new LinkedHashMap<Integer, Integer>();
		statusCancelamentos.put(AitMovimentoServices.DEFESA_PREVIA, AitMovimentoServices.CANCELAMENTO_DEFESA_PREVIA);
		statusCancelamentos.put(AitMovimentoServices.DEFESA_DEFERIDA, AitMovimentoServices.CANCELAMENTO_DEFESA_DEFERIDA);
		statusCancelamentos.put(AitMovimentoServices.DEFESA_INDEFERIDA, AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA);
		statusCancelamentos.put(AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA, AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_ENTRADA);
		statusCancelamentos.put(AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA, AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA);
		statusCancelamentos.put(AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA, AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA);
		statusCancelamentos.put(AitMovimentoServices.NIP_ENVIADA, AitMovimentoServices.CANCELAMENTO_NIP);
		statusCancelamentos.put(AitMovimentoServices.RECURSO_JARI, AitMovimentoServices.CANCELAMENTO_RECURSO_JARI);
		statusCancelamentos.put(AitMovimentoServices.JARI_COM_PROVIMENTO, AitMovimentoServices.CANCELAMENTO_JARI_COM_PROVIMENTO);
		statusCancelamentos.put(AitMovimentoServices.JARI_SEM_PROVIMENTO, AitMovimentoServices.CANCELAMENTO_JARI_SEM_PROVIMENTO);
		statusCancelamentos.put(AitMovimentoServices.RECURSO_CETRAN, AitMovimentoServices.CANCELAMENTO_RECURSO_CETRAN);
		statusCancelamentos.put(AitMovimentoServices.CETRAN_DEFERIDO, AitMovimentoServices.CANCELAMENTO_CETRAN_COM_PROVIMENTO);
		statusCancelamentos.put(AitMovimentoServices.CETRAN_INDEFERIDO, AitMovimentoServices.CANCELAMENTO_CETRAN_SEM_PROVIMENTO);
		statusCancelamentos.put(AitMovimentoServices.TRANSFERENCIA_PONTUACAO, AitMovimentoServices.CANCELAMENTO_TRANSFERENCIA_PONTUACAO);
	}
	
	private void initDadosCorreios() {
		prazosMovimentos.put(AitMovimentoServices.DADOS_CORREIO_NA, AitMovimentoServices.DADOS_CORREIO_NA);
		prazosMovimentos.put(AitMovimentoServices.DADOS_CORREIO_NP, AitMovimentoServices.DADOS_CORREIO_NP);
	}
	
	@Override
	public int getTipoDocumento(int tpDocumentoBanco) {
		return tiposDocumento.get(tpDocumentoBanco);
	}

	@Override
	public int getTipoCnh(int tpCnhBanco) {
		return tiposCnh.get(tpCnhBanco);
	}

	@Override
	public int getAssinatura(int lgAutoAssinado) {
		return lgAssinatura.get(lgAutoAssinado);
	}

	public int getMovimentoDefesa(int tpStatus) {
		return movimentoDefesa.get(tpStatus);
	}
	
	public int getPaisCnh(int cdPais) {
		return paisCnh.get(cdPais);
	}
	
	public int getMovimentoBaixa(int tpStatus) {
		return movimentoBaixa.get(tpStatus);
	}
	
	public int getMovimentoAdvertencia(int tpStatus) {
		return movimentoAdvertencia.get(tpStatus);
	}
	
	public int getPrazoMovimento(int tpStatus) {
		return prazosMovimentos.get(tpStatus);
	}
	
	public int getStatusCancelamento(int tpStatus) {
		return statusCancelamentos.get(tpStatus);
	}

	@Override
	public int getMovimento(int tpStatus) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
