package com.tivic.manager.wsdl.detran.mg.recursojari.cancelamento;


import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.recursojari.RecursoJariDadosEntrada;

public class CancelarRecursoJariDadosEntradaFactory {

	public static RecursoJariDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) throws Exception {
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		RecursoJariDadosEntrada recursoJariDadosEntrada = new RecursoJariDadosEntrada();
		recursoJariDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		recursoJariDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		recursoJariDadosEntrada.setPlaca(ait.getNrPlaca());
		recursoJariDadosEntrada.setAit(ait.getIdAit());
		recursoJariDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		recursoJariDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		recursoJariDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		recursoJariDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		recursoJariDadosEntrada.setCodigoMovimentacao(aitMovimento.getTpStatus());
		recursoJariDadosEntrada.setNumeroProcessoRecurso(aitMovimento.getNrProcesso());
		adicionarDataEntrada(aitMovimento, recursoJariDadosEntrada);
		adicionarDataEncerramento(aitMovimento, recursoJariDadosEntrada);
		//recursoJariDadosEntrada.setCodigoReclassificacaoInfracao(0);
		recursoJariDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		//recursoJariDadosEntrada.setNumeroDocumento(null);
		//recursoJariDadosEntrada.setDataPublicacaoResultadoJari(new GregorianCalendar());
		recursoJariDadosEntrada.setNmServico("CANCELAR RECURSO JARI");
		return recursoJariDadosEntrada;
	}	

	private static void adicionarDataEntrada(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) throws Exception {
		AitMovimento _jari = AitMovimentoServices.getRecursoJari(aitMovimento);
		recursoJariDadosEntrada.setDataEntradaRecurso(_jari.getDtMovimento());
	}
	
	private static void adicionarDataEncerramento(AitMovimento aitMovimento, RecursoJariDadosEntrada recursoJariDadosEntrada) {
		AitMovimento _jari = AitMovimentoServices.getResultadoJari(aitMovimento);
		if(_jari != null)
			recursoJariDadosEntrada.setDataEncerramentoRecurso(_jari.getDtMovimento());
	}
	
	private static boolean contains(int[] array, int num) {
		for(int n : array)
			if(n == num)
				return true;		
		
		return false;
	}
}
