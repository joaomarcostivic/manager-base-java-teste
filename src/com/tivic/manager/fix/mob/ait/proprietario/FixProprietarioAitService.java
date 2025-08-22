package com.tivic.manager.fix.mob.ait.proprietario;

import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.fix.mob.ait.proprietario.exceptions.BairroEmptyException;
import com.tivic.manager.fix.mob.ait.proprietario.exceptions.ConsultaDadosDetranException;
import com.tivic.manager.fix.mob.ait.proprietario.exceptions.DadosEmptyListException;
import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.bairro.BairroRepository;
import com.tivic.manager.grl.cidade.cidadeproprietario.ICorretorIDCidade;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ServicoDetranConsultaServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.lote.impressao.AitDTO;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class FixProprietarioAitService implements IFixProprietarioAitService {
	
	private AitRepository aitRepository;
	private ManagerLog managerLog;
	private ICorretorIDCidade cidadeProprietario;
	private BairroRepository bairroRepository;

	public FixProprietarioAitService() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.cidadeProprietario = (ICorretorIDCidade) BeansFactory.get(ICorretorIDCidade.class);
		this.bairroRepository = (BairroRepository) BeansFactory.get(BairroRepository.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@Override
	public void inserirDadosEnderecoAit(boolean lgAitsCandidatosLoteNip, String dtMovimento) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(true);
			managerLog.info("------- INICIADO ------- ",  new GregorianCalendar().getTime().toString());
	        List<AitDTO> aitList = buscarAitsSemDadosEndereco(lgAitsCandidatosLoteNip, dtMovimento);
			managerLog.info("QUANTIDADE: ", String.valueOf(aitList.size()));

	        for (Ait ait : aitList) {
	            
	            managerLog.info("Atualização para o AIT: ",  ait.getIdAit());
	            managerLog.info("CD AIT: ",  String.valueOf(ait.getCdAit()));
	            if(ait.getNrPlaca() != null) {
		            inserirEnderecoAit(ait, customConnection);
	            }
	        }
	        managerLog.info("------- FINALIZADO ------- ",  DateUtil.getDataAtual().toString());

	        customConnection.finishConnection();
	    } finally {
	        customConnection.closeConnection();
	    }
	}

	private List<AitDTO> buscarAitsSemDadosEndereco(boolean lgAitsCandidatosLoteNip, String dtMovimento) throws DadosEmptyListException, Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();

    	searchCriterios.addCriterios("B.dt_movimento", dtMovimento, ItemComparator.GREATER_EQUAL, Types.VARCHAR);
	    List<AitDTO> aits = searchAitsSemDadosEndereco(searchCriterios, lgAitsCandidatosLoteNip).getList(AitDTO.class);
	    
	    if(aits.isEmpty()) {
	    	throw new DadosEmptyListException();
	    } 
	    return aits;
	}

	private Search<AitDTO> searchAitsSemDadosEndereco(SearchCriterios searchCriterios, boolean lgAitsCandidatosLoteNip) throws Exception {
	    Search<AitDTO> search = new SearchBuilder<AitDTO>("mob_ait A")
	            .fields(" DISTINCT ON (A.cd_ait) A.cd_ait, * ") 
	            .addJoinTable(" JOIN mob_ait_movimento B ON(A.cd_ait = B.cd_ait) ")
	            .searchCriterios(searchCriterios)
	            .additionalCriterias(" ( "
	            		+ " cd_bairro IS NULL "
	            		+ " OR ds_nr_imovel IS NULL "
	            		+ " OR ds_logradouro IS NULL "
	            		+ "	OR nr_cep IS NULL "
	            		+ "  OR cd_cidade_proprietario IS NULL "
	            		+ " ) " 
	            )
				.additionalCriterias(incluirCondicionalNipEnviada(lgAitsCandidatosLoteNip))
				.additionalCriterias(incluirCondicionalSemNipEnviada(lgAitsCandidatosLoteNip))
	            .orderBy(" A.cd_ait DESC ") 
	            .build();
	    return search;
	}
	
	private String incluirCondicionalNipEnviada(boolean lgAitsCandidatosLoteNip) throws Exception {
		if (lgAitsCandidatosLoteNip) {
 			return " A.cd_ait IN ( "
			        + " SELECT B.cd_ait FROM mob_ait_movimento B " 
			        + " 	WHERE B.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() 
			        + " 	AND B.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
			        + " ) ";
 		}
		return null;
	}
	
	private String incluirCondicionalSemNipEnviada(boolean lgAitsCandidatosLoteNip) throws Exception {
		if (!lgAitsCandidatosLoteNip) {
 			return " A.cd_ait NOT IN ( "
			        + " SELECT B.cd_ait FROM mob_ait_movimento B " 
			        + " 	WHERE B.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() 
			        + " 	AND B.lg_enviado_detran = " + TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
			        + " ) ";
 		}
		return null;
	}
	
	private void inserirEnderecoAit(Ait ait, CustomConnection customConnection) throws Exception {
	    ServicoDetranObjeto servicoDetranObjeto = consultarPlacaAit(ait.getNrPlaca());
	    IDadosRetornoAdapter dadosRetornoAdapter = adaptarDadosRetorno(servicoDetranObjeto);

	    if (dadosRetornoAdapter != null) {
	        inserirDadosEnderecoAit(ait, dadosRetornoAdapter, customConnection);

	        aitRepository.update(ait, customConnection);
	        
	        managerLog.info("...Dados atualizados...",  ait.getIdAit());
	        managerLog.info("Logradouro: ",  ait.getDsLogradouro());
	        managerLog.info("Nº: ",   ait.getDsNrImovel());
	        managerLog.info("CD Bairro: ",  String.valueOf(ait.getCdBairro()));
	        managerLog.info("CD Cidade: ",  String.valueOf(ait.getCdCidadeProprietario()));
	    }
	}
	
	private ServicoDetranObjeto consultarPlacaAit(String nrPlaca) {
	    ServicoDetranConsultaServices servicoDetranConsultaServices = new ServicoDetranConsultaServices();
	    return servicoDetranConsultaServices.consultarPlaca(nrPlaca);
	}

	private IDadosRetornoAdapter adaptarDadosRetorno(ServicoDetranObjeto servicoDetranObjeto) throws ConsultaDadosDetranException {
	    if (servicoDetranObjeto.getDadosRetorno() instanceof ConsultarPlacaDadosRetorno) {
	        return new DadosRetornoMgAdapter((ConsultarPlacaDadosRetorno) servicoDetranObjeto.getDadosRetorno());
	    } else if (servicoDetranObjeto.getDadosRetorno() instanceof com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno) {
	        return new DadosRetornoBaAdapter((com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno) servicoDetranObjeto.getDadosRetorno());
	    } else {
	    	throw new ConsultaDadosDetranException();
	    }
	}

	private void inserirDadosEnderecoAit(Ait ait, IDadosRetornoAdapter dadosRetornoAdapter, CustomConnection customConnection) throws Exception {
	    Cidade cidade = cidadeProprietario.getCidadeById(String.valueOf(dadosRetornoAdapter.getCodigoMunicipio()));

	    if (cidade == null) {
	        managerLog.info("Não foi localizado a cidade para o AIT: ", ait.getIdAit());
	        return;
	    }
	    if (ait.getCdBairro() <= 0) {
	        Bairro bairro = getBairro(dadosRetornoAdapter.getBairro(), cidade.getCdCidade(), customConnection);
	        ait.setCdBairro(bairro.getCdBairro());
	    }
	    if (ait.getCdCidadeProprietario() <= 0) {
	        ait.setCdCidadeProprietario(cidade.getCdCidade());
	    }
	    if (ait.getDsLogradouro() == null) {
	        ait.setDsLogradouro(dadosRetornoAdapter.getLogradouro());
	    }
	    if (ait.getDsNrImovel() == null) {
	        ait.setDsNrImovel(dadosRetornoAdapter.getNumero());
	    }
	    if (ait.getNrCep() == null) {
	        ait.setNrCep(dadosRetornoAdapter.getCep());
	    }
	}

	private Bairro getBairro(String nmBairro, int cdCidade, CustomConnection customConnection) throws BairroEmptyException, Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		Bairro bairro = null;
		searchCriterios.addCriteriosEqualString("nm_bairro", nmBairro);
		searchCriterios.addCriteriosEqualInteger("cd_cidade", cdCidade);
		List<Bairro> bairroList = bairroRepository.find(searchCriterios);
		if (bairroList.isEmpty()) {
		    bairro = new Bairro();
		    bairro.setCdCidade(cdCidade);
		    bairro.setNmBairro(nmBairro);
		    bairroRepository.insert(bairro, customConnection);
		} else {
		    bairro = bairroList.get(0);
		}
		if (bairro == null) { 
		    throw new BairroEmptyException("Bairro não pode ser nulo.");
		}

		return bairro;

	}

}
