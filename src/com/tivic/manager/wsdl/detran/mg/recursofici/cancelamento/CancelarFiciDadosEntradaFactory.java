package com.tivic.manager.wsdl.detran.mg.recursofici.cancelamento;

import javax.ws.rs.BadRequestException;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.OcorrenciaDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoService;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepositoryDAO;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepositoryDAO;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepositoryDAO;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosEntrada;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;
import com.tivic.sol.search.SearchCriterios;

public class CancelarFiciDadosEntradaFactory {
	private static TabelasAuxiliares tabelaAuxiliar = new TabelasAuxiliaresMG();
	
	public static RecursoFiciDadosEntrada fazerDadoEntrada(Ait ait, AitMovimento aitMovimento, boolean lgHomologacao) throws ValidacaoException, Exception {
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		if(infracao == null)
			throw new ValidacaoException("Movimento sem infração");
		Orgao orgao = OrgaoServices.getOrgaoUnico();	
		Ocorrencia ocorrencia = OcorrenciaDAO.get(aitMovimento.getCdOcorrencia());
		if(ocorrencia == null)
			throw new ValidacaoException("Movimento sem ocorrência");
		Documento documento = buscarDocumento(ait.getCdAit());
		ApresentacaoCondutor apresentacaoCondutor = buscarFICI(documento);
		AitMovimento movimentoFICI = new AitMovimentoService().getMovimentoTpStatus(ait.getCdAit(), TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		
		RecursoFiciDadosEntrada incluirFiciDadosEntrada = new RecursoFiciDadosEntrada();
		incluirFiciDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		incluirFiciDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		incluirFiciDadosEntrada.setPlaca(ait.getNrPlaca());
		incluirFiciDadosEntrada.setAit(ait.getIdAit());
		incluirFiciDadosEntrada.setNumeroProcessamento(ait.getNrControle());
		incluirFiciDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		incluirFiciDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		incluirFiciDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirFiciDadosEntrada.setCodigoMovimentacao(2); //Cancelamento
		incluirCnh(apresentacaoCondutor, incluirFiciDadosEntrada);
		incluirCondutor(apresentacaoCondutor, incluirFiciDadosEntrada);
		incluirFiciDadosEntrada.setNumeroProtocolo(formatNrProcesso(aitMovimento.getNrProcesso()));
		incluirFiciDadosEntrada.setParecerFici(setParecer(documento));
		incluirFiciDadosEntrada.setDataParecerFici(movimentoFICI.getDtMovimento());
		incluirFiciDadosEntrada.setCodigoParecerFici(Util.fillNum(ocorrencia.getIdMovimentacao(), 3));
		incluirFiciDadosEntrada.setDataProtocolo(movimentoFICI.getDtMovimento());
		incluirFiciDadosEntrada.setNmServico("CANCELAR FICI");
		return incluirFiciDadosEntrada;
	}
		
	private static void incluirCnh(ApresentacaoCondutor apresentacaoCondutor, RecursoFiciDadosEntrada incluirFiciDadosEntrada) throws ValidacaoException, Exception{
		if(apresentacaoCondutor.getTpModeloCnh() > 0) {
			incluirFiciDadosEntrada.setModeloCnh(tabelaAuxiliar.getTipoCnh(apresentacaoCondutor.getTpModeloCnh()));
			if(incluirFiciDadosEntrada.getModeloCnh() == AitServices.TP_CNH_HABILITACAO_ESTRANGEIRA_MG) {
				incluirDadosCNHExtrangeira(apresentacaoCondutor, incluirFiciDadosEntrada);
			} else {
				incluirDadosCNHNacional(apresentacaoCondutor, incluirFiciDadosEntrada);
			}
		}		
	}
	
	private static void incluirDadosCNHNacional(ApresentacaoCondutor apresentacaoCondutor, RecursoFiciDadosEntrada incluirFiciDadosEntrada) {
		if (apresentacaoCondutor.getUfCnh() != null && apresentacaoCondutor.getUfCnh().trim() != "") {
			incluirFiciDadosEntrada.setUfCnh(apresentacaoCondutor.getUfCnh());
		}
		if (apresentacaoCondutor.getNrCnh() != null && apresentacaoCondutor.getNrCnh().trim() != "") {
			incluirFiciDadosEntrada.setNumeroCnh(apresentacaoCondutor.getNrCnh());
		}
	}
	
	private static void incluirDadosCNHExtrangeira(ApresentacaoCondutor apresentacaoCondutor, RecursoFiciDadosEntrada incluirFiciDadosEntrada) {
		incluirFiciDadosEntrada.setCodigoPaisCnh(Integer.valueOf(apresentacaoCondutor.getIdPaisCnh()));
		incluirFiciDadosEntrada.setUfCnh("EX");
	}
	
	private static Documento buscarDocumento(int cdAit) throws Exception {
		AitMovimento movimentoFICI = new AitMovimentoService().getMovimentoTpStatus(cdAit, TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("cd_movimento", movimentoFICI.getCdMovimento());
		AitMovimentoDocumento aitMovimentoDocumento = new AitMovimentoDocumentoRepositoryDAO().find(searchCriterios).get(0);
		Documento documento = new DocumentoRepositoryDAO().get(aitMovimentoDocumento.getCdDocumento()); 
		return documento;
	}
	
	private static ApresentacaoCondutor buscarFICI(Documento documento) throws BadRequestException, Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_documento", documento.getCdDocumento());
		ApresentacaoCondutor apresentacaoCondutor = new ApresentacaoCondutorRepositoryDAO().find(searchCriterios).get(0);
		return apresentacaoCondutor;
	}
	
	private static void incluirCondutor(ApresentacaoCondutor apresentacaoCondutor, RecursoFiciDadosEntrada incluirFiciDadosEntrada) throws ValidacaoException, Exception {
		if (apresentacaoCondutor.getNmCondutor() != null && apresentacaoCondutor.getNmCondutor().trim() != "") 
		{
			incluirFiciDadosEntrada.setNomeCondutor(apresentacaoCondutor.getNmCondutor() + " ");
		}
		if (apresentacaoCondutor.getNrRg() != null && apresentacaoCondutor.getNrRg().trim() != "")
		{
			incluirFiciDadosEntrada.setRgCondutor(apresentacaoCondutor.getNrRg());
		}
		if (apresentacaoCondutor.getNrCpfCnpj() != null && apresentacaoCondutor.getNrCpfCnpj().trim() != "") 
		{
			incluirFiciDadosEntrada.setCpfCondutor(apresentacaoCondutor.getNrCpfCnpj());
		}
	}
	
	private static String formatNrProcesso(String entry) throws ValidacaoException {
		Cidade _cidadeOrgao = OrgaoServices.getCidadeOrgaoAutuador();
		
		if(_cidadeOrgao.getNmCidade().equalsIgnoreCase("MARIANA") || _cidadeOrgao.getNmCidade().equalsIgnoreCase("ITAUNA")) 
			return getNrProcessoMariana(entry);
		 else 
			return getNrProcessoGeral(entry);
		
	}
	
	private static String getNrProcessoMariana(String entry) throws ValidacaoException {
		
		String nrProtocolo = entry.replaceAll("[A-Za-z/-]", "");
		return nrProtocolo;
	}
	
	private static String getNrProcessoGeral(String entry) throws ValidacaoException {
		String num = entry.replaceAll("/", "");
		
		if(!Util.isNumber(num))
			throw new ValidacaoException("Nº do protocolo é inválido");
		
		if(entry.length() <= 16)
			return Util.fillLong(Long.parseLong(num), 16);
		else
			return entry;
	}
	
	private static String setParecer (Documento documento) throws Exception {
		int codigoSituacaoDeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		int codigoSituacaoIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0);
		if(documento.getCdSituacaoDocumento() == codigoSituacaoDeferida)
			return "A";
		if(documento.getCdSituacaoDocumento() == codigoSituacaoIndeferida)
			return "R";
		return "";
	}
	
}
