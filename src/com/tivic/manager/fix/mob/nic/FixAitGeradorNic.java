package com.tivic.manager.fix.mob.nic;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.grl.parametro.ParametroService;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDTO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.agente.AgenteService;
import com.tivic.manager.mob.agente.IAgenteService;
import com.tivic.manager.mob.ait.AitService;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepositoryDAO;
import com.tivic.manager.util.cdi.InjectApplicationBuilder;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadual;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.soap.ArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.cdi.InicializationBeans;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class FixAitGeradorNic {
	public static void main(String[] args) throws Exception {
		InicializationBeans.init(new InjectApplicationBuilder());
		fixAitOrigemNic();
	}
	
	public static void fixAitOrigemNic() throws Exception {
		CustomConnection customConnection = null;
		try {
			ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			List<AitDTO> aitsList = searchAits(customConnection);
			int totalVerificados = 0;
			int totalCorrigidos = 0;
			for(AitDTO ait : aitsList) {
				String idAitGeradora = searchFromBaseEstadual(ait);
				if (idAitGeradora != null && !idAitGeradora.equals("")) {
					setCdAitOrigem(ait, idAitGeradora, customConnection);
					setMovimentoNicEnviado(ait, customConnection);
					managerLog.info("\nId. AIT NIC: " + ait.getIdAit() + " -------- Id. AIT origem: " + idAitGeradora, "");
					totalCorrigidos++;
				}
				totalVerificados++;
			}
			managerLog.info("\n\nTotal de AITs corrigidos: " + totalCorrigidos, "");
			managerLog.info("\n\nTotal de AITs verificados: " + totalVerificados, "");
			customConnection.finishConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static List<AitDTO> searchAits(CustomConnection customConnection) throws Exception {
		IParametroService parametroService = new ParametroService();
		Integer cdInfracaoNic = (parametroService.getValorOfParametroAsInt("MOB_CD_TIPO_INFRACAO_NIC")).getNrValorParametro();
		List<AitDTO> aits = new ArrayList<AitDTO>();
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<AitDTO> search = new SearchBuilder<AitDTO>("mob_ait A")
				.fields(" A.cd_ait_origem, A.* ")
				.additionalCriterias("A.cd_infracao = " + cdInfracaoNic)
				.additionalCriterias("cd_ait_origem IS NULL")
				.customConnection(customConnection)
				.searchCriterios(searchCriterios)
				.build();
		aits = search.getList(AitDTO.class);
		if(aits == null) {
			throw new NoContentException("Nenhum AIT encontrado com esse nrCodDetran");
		}
		return aits;
	}
	
	private static String searchFromBaseEstadual(Ait ait) throws Exception {		
		AitDetranObject aitDetranObject = new AitDetranObject(ait);
        ConsultaAutoBaseEstadual consultaAutoBaseEstadual = new ConsultaAutoBaseEstadual();
        consultaAutoBaseEstadual.setArquivoConfiguracao(new ArquivoConfiguracaoMg());
        ServicoDetranObjeto servicoDetranObjeto = consultaAutoBaseEstadual.executar(aitDetranObject);
        ConsultaAutoBaseEstadualDadosRetorno consultaAutoBaseEstadualDadosRetorno = (ConsultaAutoBaseEstadualDadosRetorno) servicoDetranObjeto.getDadosRetorno();
        return consultaAutoBaseEstadualDadosRetorno.getAitGeradora();
	}
	
	private static void setCdAitOrigem(AitDTO ait, String idAitGeradora, CustomConnection customConnection) throws Exception {
		IAitService aitService = new AitService();
		IAgenteService agenteService = new AgenteService();
		Ait aitOrigem = aitService.getById(idAitGeradora, customConnection);
		Agente agenteOriginaria = (agenteService.get(aitOrigem.getCdAgente(), customConnection)).get(0);
		ait.setCdAitOrigem(aitOrigem.getCdAit());
		ait.setCdAgente(agenteOriginaria.getCdAgente());
		aitService.update(ait, customConnection);
	}
	
	private static void setMovimentoNicEnviado(AitDTO ait, CustomConnection customConnection) throws Exception {
		AitMovimentoRepository aitMovimentoRepository = new AitMovimentoRepositoryDAO();
		List<AitMovimento> listaNicEnviado = aitMovimentoRepository.find(searchCriteriosMovimentoNicEnviado(ait), customConnection);
		if (listaNicEnviado.isEmpty()) {
			AitMovimento movimentoNicEnviado = new AitMovimento();
			AitMovimento movimentoRegistrado = (aitMovimentoRepository.find(searchCriteriosMovimentoRegistrado(ait), customConnection)).get(0);
			movimentoNicEnviado.setTpStatus(TipoStatusEnum.NIC_ENVIADA.getKey());
			movimentoNicEnviado.setCdAit(ait.getCdAit());
			movimentoNicEnviado.setLgEnviadoDetran(1);
			movimentoNicEnviado.setCdUsuario(movimentoRegistrado.getCdUsuario());
			(movimentoRegistrado.getDtMovimento()).add(GregorianCalendar.SECOND, 1);
			movimentoNicEnviado.setDtMovimento((movimentoRegistrado.getDtMovimento()));
			movimentoNicEnviado.setDtRegistroDetran(movimentoRegistrado.getDtRegistroDetran());
			aitMovimentoRepository.insert(movimentoNicEnviado, customConnection);
		}
	}
	
	private static SearchCriterios searchCriteriosMovimentoNicEnviado(AitDTO ait) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", ait.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.NIC_ENVIADA.getKey(), true);
		return searchCriterios;
	}
	
	private static SearchCriterios searchCriteriosMovimentoRegistrado(AitDTO ait) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", ait.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.REGISTRO_INFRACAO.getKey(), true);
		return searchCriterios;
	}
}
