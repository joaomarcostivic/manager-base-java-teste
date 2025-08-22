package com.tivic.manager.fix.mob.ait.task;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.fix.mob.ait.proprietario.DadosRetornoBaAdapter;
import com.tivic.manager.fix.mob.ait.proprietario.DadosRetornoMgAdapter;
import com.tivic.manager.fix.mob.ait.proprietario.IDadosRetornoAdapter;
import com.tivic.manager.fix.mob.ait.proprietario.exceptions.BairroEmptyException;
import com.tivic.manager.fix.mob.ait.proprietario.exceptions.ConsultaDadosDetranException;
import com.tivic.manager.fix.mob.ait.proprietario.exceptions.DadosEmptyListException;
import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.bairro.BairroRepository;
import com.tivic.manager.grl.cidade.cidadeproprietario.ICorretorIDCidade;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ServicoDetranConsultaServices;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaRepository;
import com.tivic.manager.mob.inconsistencias.TipoSituacaoInconsistencia;
import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ClientFixEnderecoNotificacao implements IClientFixEnderecoNotificacao {

	private ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	private CustomConnection customConnection;
	private IParametroRepository parametroRepository;
	private BairroRepository bairroRepository;
	private ICorretorIDCidade cidadeProprietario;
	private AitRepository aitRepository;
	private AitInconsistenciaRepository aitInconsistenciaRepository;
	private int tpSemEnderecoCorrespondencia;
	
	public ClientFixEnderecoNotificacao() throws Exception {
		this.customConnection = new CustomConnection();
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.bairroRepository = (BairroRepository) BeansFactory.get(BairroRepository.class);
		this.cidadeProprietario = (ICorretorIDCidade) BeansFactory.get(ICorretorIDCidade.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitInconsistenciaRepository = (AitInconsistenciaRepository) BeansFactory.get(AitInconsistenciaRepository.class);
	}
	
	@Override
    public void taskFixEnderecoNotificacao() throws Exception {
        try {
            this.customConnection.initConnection(true);
            managerLog.info("TASK CORREÇÃO ENDEREÇO DE CORRESPONDÊNCIA DE NOTIFICAÇÃO INICIADA: ", new GregorianCalendar().getTime().toString());
            List<Ait> aitList = buscarAitsEndereçoInconsistente();
            managerLog.info("QUANTIDADE DE REGISTROS: ", String.valueOf(aitList.size()));
            for (Ait ait : aitList) {
            	managerLog.info("Atualização para o AIT: ",  ait.getIdAit());
            	managerLog.info("Placa: ",  ait.getNrPlaca());
            	if(ait.getCdAit() > 0) {
            		inserirEnderecoAit(ait, customConnection);
            	}
	        }
            this.customConnection.finishConnection();
        } catch (NoContentException nce) {
            managerLog.info("NENHUM AIT COM ENDEREÇO INCONSISTENTE ENCONTRADO: ", new GregorianCalendar().getTime().toString());
        } catch (Exception e) {
            managerLog.showLog(e);
        } finally {
            this.customConnection.closeConnection();
            managerLog.info("TASK CORREÇÃO ENDEREÇO DE CORRESPONDÊNCIA DE NOTIFICAÇÃO FINALIZADA: ", new GregorianCalendar().getTime().toString());
        }
    }
	
	private List<Ait> buscarAitsEndereçoInconsistente() throws DadosEmptyListException, Exception {
        this.tpSemEnderecoCorrespondencia = parametroRepository.getValorOfParametroAsInt("MOB_ENDERECO_NOTIFICACAO");
        if(tpSemEnderecoCorrespondencia <= 0) {
        	throw new ValidacaoException("Parâmetro MOB_ENDERECO_NOTIFICACAO não configurado.");
        }
	    SearchCriterios searchCriterios = new SearchCriterios();
    	searchCriterios.addCriteriosEqualInteger("B.cd_inconsistencia", this.tpSemEnderecoCorrespondencia, true);
    	searchCriterios.addCriteriosEqualInteger("B.st_inconsistencia", TipoSituacaoInconsistencia.PENDENTE.getKey(), true);
	    List<Ait> aitList = searchAitsEndereçoInconsistente(searchCriterios).getList(Ait.class);
	    
	    if(aitList.isEmpty()) {
	    	throw new DadosEmptyListException();
	    } 
	    return aitList;
	}
	
	public Search<Ait> searchAitsEndereçoInconsistente(SearchCriterios searchCriterios) throws Exception {
		 Search<Ait> search = new SearchBuilder<Ait>(" mob_ait A")
	            .fields(" A.* ")    
	            .addJoinTable(" JOIN mob_ait_inconsistencia B ON (A.cd_ait = B.cd_ait) ")
	            .addJoinTable(" JOIN mob_inconsistencia C ON (C.cd_inconsistencia = B.cd_inconsistencia) ")
				.searchCriterios(searchCriterios)
	            .orderBy(" A.dt_infracao DESC ")
	            .build();
	    return search;
	}
	
	private void inserirEnderecoAit(Ait ait, CustomConnection customConnection) throws Exception {
	    ServicoDetranObjeto servicoDetranObjeto = consultarPlacaAit(ait.getNrPlaca());
	    IDadosRetornoAdapter dadosRetornoAdapter = adaptarDadosRetorno(servicoDetranObjeto);

	    if (dadosRetornoAdapter != null) {
	        inserirDadosEnderecoAit(ait, dadosRetornoAdapter, customConnection);

	        aitRepository.update(ait, customConnection);
	        corrigirIncosistenciaAit(ait.getCdAit(), customConnection);
	        
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
	
	private void corrigirIncosistenciaAit(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("cd_inconsistencia", this.tpSemEnderecoCorrespondencia, true);
		List<AitInconsistencia> aitInconsistenciaList = aitInconsistenciaRepository.find(searchCriterios, customConnection);
		if (aitInconsistenciaList != null && !aitInconsistenciaList.isEmpty()) {
            for (AitInconsistencia aitInconsistencia : aitInconsistenciaList) {
            	aitInconsistencia.setStInconsistencia(TipoSituacaoInconsistencia.RESOLVIDO.getKey());
            	aitInconsistenciaRepository.update(aitInconsistencia, customConnection);
            }
        }
	}
	
}

