package com.tivic.manager.fix.ptc.protocolo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.documento.DocumentoRepositoryDAO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorService;
import com.tivic.manager.ptc.protocolosv3.cancelamento.CancelaEntradaProtocolo;
import com.tivic.manager.ptc.protocolosv3.documento.situacao.CancelamentoDocumento;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class FixProtocolos {
	
	private ManagerLog managerLog;
	
	public FixProtocolos() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	static int cdCancelamento = ParametroServices.getValorOfParametroAsInteger("CD_FASE_CANCELADO", 0);
	
	public static void fixCancelaProtocolo(Integer a) {
		CustomConnection customConnection = null;
		try {
			ManagerLog managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
			customConnection = new CustomConnection();
			customConnection.initConnection(true);
			List<ProtocoloSearchDTO> protocolosSearch = searchProtocolos(customConnection);
			List<ProtocoloDTO> protocolos = montaProtocoloDTO(protocolosSearch);
			List<String> protocolosCancelados = new ArrayList<String>();
			
			int total = 0;
			for(ProtocoloDTO protocolo: protocolos) {
				total++;
				cancelaProtocolo(protocolo);
				protocolosCancelados.add("" + protocolo.getAitMovimento().getCdAit());
				continue;
			}
			customConnection.finishConnection();
			
			for(String aitCancelado: protocolosCancelados) {
				managerLog.info("Protocolo cancelado do AIT: ", aitCancelado );
			}
			managerLog.info("Total de AITs cancelados: ", "" + total);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				customConnection.closeConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static List<ProtocoloSearchDTO> searchProtocolos(CustomConnection customConnection) throws Exception {
		List<ProtocoloSearchDTO> protocolos = new ArrayList<ProtocoloSearchDTO>();
		SearchCriterios searchCriterios = new SearchCriterios();
		Search<ProtocoloSearchDTO> search = new SearchBuilder<ProtocoloSearchDTO>("mob_ait_movimento A")
				.fields(" A.dt_movimento, A.tp_status, B.id_ait, B.cd_ait, B.dt_infracao, D.nr_documento, D.cd_documento, D.dt_protocolo, D.tp_documento, "
						+ " E.nm_tipo_documento, E.cd_tipo_documento, F.nm_fase, H.cd_situacao_documento, "
						+ " H.nm_situacao_documento, G.cd_apresentacao_condutor, "
						+ "	 D.dt_protocolo + INTERVAL '1 year' AS dt_prazo")
			.addJoinTable(" JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
			.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.cd_ait = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
			.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
			.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
			.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
			.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
			.addJoinTable(" LEFT JOIN ptc_apresentacao_condutor G ON (D.cd_documento = G.cd_documento)")
			.customConnection(customConnection)
			.additionalCriterias("A.lg_enviado_detran in (" + TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey() 
															+ "," + TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey() + ")" 
															+ " AND F.cd_fase <> " + cdCancelamento)
			.additionalCriterias("A.nr_processo = D.nr_documento")
			.searchCriterios(searchCriterios)
			.orderBy(" D.dt_protocolo DESC ")
		.build();
			
		protocolos = search.getList(ProtocoloSearchDTO.class);
	    if (protocolos == null) {
	        throw new NoContentException("Nenhum protocolo encontrado.");
	    }

	    return protocolos;
	}
	
	private static void cancelaProtocolo(ProtocoloDTO protocolo) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		DocumentoRepository documentoRepository = new DocumentoRepositoryDAO();
		CancelaEntradaProtocolo cancelaDocumento = new CancelaEntradaProtocolo();
		if(protocolo.getDocumento().getCdTipoDocumento() == TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey()) {
			cancelaDocumento.cancelar(protocolo, customConnection);
		}else {
			Documento documento = documentoRepository.get(protocolo.getDocumento().getCdDocumento(), customConnection);
			documento.setCdFase(cdCancelamento);
			documentoRepository.update(documento, new CustomConnection());
		}
	}
	
	private static List<ProtocoloDTO> montaProtocoloDTO(List<ProtocoloSearchDTO> protocoloSearchDTO) throws Exception {
	    List<ProtocoloDTO> protocolos = new ArrayList<>();
	    for (ProtocoloSearchDTO ObjetoProtocoloSearchDTO : protocoloSearchDTO) {
	        ProtocoloDTO protocolo = new ProtocoloDTO();
	        protocolo.getAitMovimento().setCdMovimento(cdMovimento(ObjetoProtocoloSearchDTO.getCdAit(), ObjetoProtocoloSearchDTO.getTpStatus(), new CustomConnection()));
	        protocolo.getAitMovimento().setCdAit(ObjetoProtocoloSearchDTO.getCdAit());
	        protocolo.getAitMovimento().setDtMovimento(ObjetoProtocoloSearchDTO.getDtMovimento());
	        protocolo.getAitMovimento().setTpStatus(ObjetoProtocoloSearchDTO.getTpStatus());
	        protocolo.getAit().setIdAit(ObjetoProtocoloSearchDTO.getIdAit());
	        protocolo.getAit().setCdAit(ObjetoProtocoloSearchDTO.getCdAit());
	        protocolo.getAit().setDtInfracao(ObjetoProtocoloSearchDTO.getDtInfracao());
	        protocolo.getDocumento().setNrDocumento(ObjetoProtocoloSearchDTO.getNrDocumento());
	        protocolo.getDocumento().setCdDocumento(ObjetoProtocoloSearchDTO.getCdDocumento());
	        protocolo.getDocumento().setDtProtocolo(ObjetoProtocoloSearchDTO.getDtProtocolo());
	        protocolo.getTipoDocumento().setNmTipoDocumento(ObjetoProtocoloSearchDTO.getNmTipoDocumento());
	        protocolo.getTipoDocumento().setCdTipoDocumento(ObjetoProtocoloSearchDTO.getCdTipoDocumento());
	        protocolo.getFase().setNmFase(ObjetoProtocoloSearchDTO.getNmFase());
	        protocolo.getSituacaoDocumento().setCdSituacaoDocumento(ObjetoProtocoloSearchDTO.getCdSituacaoDocumento());
	        
	        protocolos.add(protocolo);
	    }

	    if (protocolos.isEmpty()) {
	        throw new NoContentException("Nenhum protocolo encontrado.");
	    }
	    
	    return protocolos;
	}
	
	private static int cdMovimento(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		AitMovimento cdMovimento = new AitMovimento();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, cdAit > 0);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus, tpStatus > 0);
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.cd_movimento")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		
		List<AitMovimento> aitMovimento = search.getList(AitMovimento.class);
		if (aitMovimento.isEmpty()) {
			throw new NoContentException("Nenhum movimento encontrado."); 
		}

		cdMovimento = aitMovimento.get(0);
		
	    return cdMovimento.getCdMovimento();
	}
}
