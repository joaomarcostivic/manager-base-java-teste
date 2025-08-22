package com.tivic.manager.wsdl.detran.mg.cancelarautoinfracao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.ocorrencia.OcorrenciaRepository;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;

public class CancelarAutoInfracaoDadosEntradaFactory {
	
	private static OcorrenciaRepository ocorrenciaRepository;
	private static InfracaoRepository infracaoRepository;
	
	public static CancelarAutoInfracaoDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject, boolean lgHomologacao) throws ValidacaoException, Exception{
		ocorrenciaRepository = (OcorrenciaRepository) BeansFactory.get(OcorrenciaRepository.class);
		infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
		
		Ait ait = aitDetranObject.getAit();
		AitMovimento aitMovimento = aitDetranObject.getAitMovimento();
		Ocorrencia ocorrencia = ocorrenciaRepository.get(aitDetranObject.getAitMovimento().getCdOcorrencia());
		if(ocorrencia == null)
			throw new ValidacaoException("Faltando ocorrência no movimento");
		
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = infracaoRepository.get(ait.getCdInfracao());
		
		CancelarAutoInfracaoDadosEntrada cancelarAutoInfracaoDadosEntrada = new CancelarAutoInfracaoDadosEntrada();
		cancelarAutoInfracaoDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		cancelarAutoInfracaoDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		cancelarAutoInfracaoDadosEntrada.setPlaca(ait.getNrPlaca());
		cancelarAutoInfracaoDadosEntrada.setAit(ait.getIdAit());
		cancelarAutoInfracaoDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		cancelarAutoInfracaoDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		cancelarAutoInfracaoDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		cancelarAutoInfracaoDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		cancelarAutoInfracaoDadosEntrada.setCodigoMovimentacao(ocorrencia.getIdMovimentacao());
		cancelarAutoInfracaoDadosEntrada.setDataMovimentacao(aitMovimento.getDtMovimento());
		cancelarAutoInfracaoDadosEntrada.setNumeroDocumento(String.valueOf(aitMovimento.getCdAit()) + String.valueOf(aitMovimento.getCdMovimento()));
		//cancelarAutoInfracaoDadosEntrada.setValorPago(0.0);//:TODO Buscar de AitPagamento
		
		cancelarAutoInfracaoDadosEntrada.setNmServico("CANCELAR AUTO INFRACAO");
		return cancelarAutoInfracaoDadosEntrada;
	}
}
