package com.tivic.manager.wsdl.detran.mg.recursofici;

import java.util.List;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoDAO;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.OcorrenciaDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorDAO;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepositoryDAO;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.search.SearchCriterios;

public class IncluirFiciDadosEntradaFactory {
	
	private static ApresentacaoCondutorRepository apresentacaoCondutorRepository = new ApresentacaoCondutorRepositoryDAO();
	
	public static RecursoFiciDadosEntrada fazerDadoEntrada(Ait ait, AitMovimento aitMovimento, boolean lgHomologacao) throws ValidacaoException, Exception{
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		if(infracao == null)
			throw new ValidacaoException("Movimento sem infração");
		Orgao orgao = OrgaoServices.getOrgaoUnico();	
		Ocorrencia ocorrencia = OcorrenciaDAO.get(aitMovimento.getCdOcorrencia());
		if(ocorrencia == null)
			throw new ValidacaoException("Movimento sem ocorrência");
		RecursoFiciDadosEntrada incluirFiciDadosEntrada = new RecursoFiciDadosEntrada();
		incluirFiciDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		incluirFiciDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		incluirFiciDadosEntrada.setPlaca(ait.getNrPlaca());
		incluirFiciDadosEntrada.setAit(ait.getIdAit());
		incluirFiciDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		incluirFiciDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		incluirFiciDadosEntrada.setNomeCondutor(ait.getNmCondutor());
		incluirFiciDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		incluirFiciDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirFiciDadosEntrada.setCodigoMovimentacao(1);//Inclusão
		incluirDadosCondutor(aitMovimento, incluirFiciDadosEntrada);
		formatNrProcesso(aitMovimento, incluirFiciDadosEntrada);
		incluirFiciDadosEntrada.setParecerFici(setParecer(aitMovimento));
		incluirFiciDadosEntrada.setDataParecerFici(aitMovimento.getDtMovimento());
		incluirFiciDadosEntrada.setCodigoParecerFici(Util.fillNum(ocorrencia.getIdMovimentacao(), 3));
		incluirFiciDadosEntrada.setDataProtocolo(aitMovimento.getDtMovimento());
		incluirFiciDadosEntrada.setNmServico("INCLUIR FICI");
		return incluirFiciDadosEntrada;
	}
	
	private static void incluirDadosCondutor(AitMovimento aitMovimento, RecursoFiciDadosEntrada dadosEntrada) throws ValidacaoException, Exception{
		if(hasApresentacaoCondutor(aitMovimento)) {
			new DadosCondutorDirector(dadosEntrada, aitMovimento).constructFiciApresentacaoCondutor();
		} else {
			new DadosCondutorDirector(dadosEntrada, aitMovimento).constructFiciFormulario();
		}
	}
	
	private static Boolean hasApresentacaoCondutor(AitMovimento aitMovimento) throws ValidacaoException, Exception{
		AitMovimentoDocumento aitMovimentoDocumento = AitMovimentoDocumentoDAO.getAit(aitMovimento.getCdAit(), aitMovimento.getCdMovimento());
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("cd_documento",aitMovimentoDocumento.getCdDocumento(), aitMovimentoDocumento.getCdDocumento() > 0);
		List<ApresentacaoCondutor> ficis = apresentacaoCondutorRepository.find(criterios);
		return !ficis.isEmpty();
	} 
	

		
	private static void formatNrProcesso(AitMovimento aitMovimento, RecursoFiciDadosEntrada dadosEntrada) throws ValidacaoException {	
		String nrProcesso = aitMovimento.getNrProcesso().replaceAll("[A-Za-z/-]", "");
	    if (!Util.isNumber(nrProcesso)) {
	        throw new ValidacaoException("Nº do protocolo é inválido");
	    } else {
	    	dadosEntrada.setNumeroProtocolo(nrProcesso);	    		    	
	    }
	}
	
	private static String setParecer (AitMovimento aitMovimento) {
		AitMovimentoDocumento _amc = AitMovimentoDocumentoDAO.getAit(aitMovimento.getCdAit(), aitMovimento.getCdMovimento());
		Documento _documento = new Documento();
		
		if(_amc != null)
			_documento = DocumentoDAO.get(_amc.getCdDocumento());
		else
			return "";
		
		int codigoSituacaoDeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		int codigoSituacaoIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0);
		
		if(_documento.getCdSituacaoDocumento() == codigoSituacaoDeferida)
			return "A";
		
		if(_documento.getCdSituacaoDocumento() == codigoSituacaoIndeferida)
			return "R";
		
		return "";
	}
	
	private boolean isCnhEstrangeira(int cdDocumento) {
		ApresentacaoCondutor condutor = ApresentacaoCondutorDAO.get(cdDocumento);
		return condutor.getTpModeloCnh() == TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey() ?
		true : false;
	}
	
}
