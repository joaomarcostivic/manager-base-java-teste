package com.tivic.manager.wsdl.detran.mg.recursonic;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.sol.cdi.BeansFactory;

public class IncluirNICDadosEntradaFactory {
	private static AitRepository aitRepository;
	private static final int PREFEITURA = 3; 
	private static final int MOVIMENTO_NIC = 1;
	private static final int FATOR_MULTIPLICADOR = 2; 
	
	public static RecursoNICDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		Ait ait = aitDetranObject.getAit();
		Ait aitGeradora = aitRepository.get(aitDetranObject.getAit().getCdAitOrigem());
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(aitGeradora.getCdInfracao());
		RecursoNICDadosEntrada recursoNICDadosEntrada = new RecursoNICDadosEntrada();
		recursoNICDadosEntrada.setOrigemSolicitacao(PREFEITURA);
		recursoNICDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		recursoNICDadosEntrada.setPlaca(ait.getNrPlaca());
		recursoNICDadosEntrada.setAit(ait.getIdAit());
		recursoNICDadosEntrada.setDataInfracao(ait.getDtInfracao());
		recursoNICDadosEntrada.setCodigoMovimentacao(MOVIMENTO_NIC);
		recursoNICDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		recursoNICDadosEntrada.setValorMulta(ait.getVlMulta());
		recursoNICDadosEntrada.setCodigoInfracaoGeradora(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		recursoNICDadosEntrada.setCodigoDesdobramentoInfracaoGeradora(String.valueOf(infracao.getNrCodDetran()).substring(4));
		recursoNICDadosEntrada.setAitGeradora(aitGeradora.getIdAit());
		recursoNICDadosEntrada.setNumeroProcessamentoGeradora(aitGeradora.getNrControle());
		recursoNICDadosEntrada.setFatorMultiplicador(FATOR_MULTIPLICADOR);
		recursoNICDadosEntrada.setCodigoRenainfGeradora(ait.getNrRenainf());
		return recursoNICDadosEntrada;	
	}
}
