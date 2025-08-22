package com.tivic.manager.ptc.protocolosv3;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.NoContentException;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.CodigoCancelamentoMovimentoMap;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.MovimentoNaoAtualizaStatusAit;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import sol.dao.ItemComparator;

public class DocumentoAtualizaStatusAIT {
	
	private ArrayList<Integer> documentos = new ArrayList<Integer>();
	private MovimentoNaoAtualizaStatusAit movimentoNaoAtualizaStatusAit;
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoService;
	
	public DocumentoAtualizaStatusAIT() throws Exception {
		this.movimentoNaoAtualizaStatusAit = new MovimentoNaoAtualizaStatusAit();
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		adicionarDocumentos();
	}
	
	private void adicionarDocumentos() {
		this.documentos.add(TipoDocumentoProtocoloEnum.DEFESA_PREVIA.getKey());
		this.documentos.add(TipoDocumentoProtocoloEnum.DEFESA_PREVIA_ADVERTENCIA.getKey());
		this.documentos.add(TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey());
		this.documentos.add(TipoDocumentoProtocoloEnum.RECURSO_CETRAN.getKey());
	}
	
	public boolean verificar(int cdTipoDocumento) {
		return this.documentos.contains(cdTipoDocumento);
	}
	
	public void atualizarInserido(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		Ait ait = this.aitRepository.get(protocolo.getAit().getCdAit(), customConnection);
		ait.setCdMovimentoAtual(protocolo.getAitMovimento().getCdMovimento());
		ait.setTpStatus(Integer.parseInt(protocolo.getTipoDocumento().getIdTipoDocumento()));
		aitRepository.update(ait, customConnection);
	}
	
	public void atualizarCancelado(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		Ait ait = this.aitRepository.get(protocolo.getAit().getCdAit(), customConnection);
		AitMovimento movimentoValido = pegarUltimoMovimentoValido(protocolo, customConnection);
		ait.setCdMovimentoAtual(movimentoValido.getCdMovimento());
		ait.setTpStatus(movimentoValido.getTpStatus());
		this.aitRepository.update(ait, customConnection);
	}
	
	private AitMovimento pegarUltimoMovimentoValido(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		AitMovimento movimentoValido = new AitMovimento();
		SearchCriterios criterios = criteriosMovimento(protocolo.getAit().getCdAit(), protocolo.getTipoDocumento().getIdTipoDocumento());
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.cd_ait, A.cd_movimento, A.tp_status, A.dt_movimento")
				.searchCriterios(criterios)
				.orderBy(" A.dt_movimento DESC ")
				.customConnection(customConnection)
				.build();
		aitMovimentoList = search.getList(AitMovimento.class);
		if (aitMovimentoList.isEmpty()) {
			throw new NoContentException("Nenhum movimento encontrado para este AIT.");
		}
		movimentoValido = verificarCancelamentoMovimento(aitMovimentoList, customConnection);
		return movimentoValido;
	}
	
	private AitMovimento verificarCancelamentoMovimento(List<AitMovimento> aitMovimentoList, CustomConnection customConnection) throws Exception {
		AitMovimento movimentoValido = new AitMovimento();
		for (AitMovimento aitMovimento : aitMovimentoList) {
			int codigoCancelamentoMovimento = new CodigoCancelamentoMovimentoMap().get(aitMovimento.getTpStatus());
			AitMovimento movimentoCancelado = this.aitMovimentoService.getMovimentoTpStatus(aitMovimento.getCdAit(), codigoCancelamentoMovimento);
			if (movimentoCancelado.getCdMovimento() <= 0) {
				movimentoValido = aitMovimento;
				break;
			}
		}
		if (movimentoValido.getCdMovimento() <= 0) {
			throw new NoContentException("Nenhum movimento valido encontrado para este AIT.");
		}
		return movimentoValido;
	}
	
	private SearchCriterios criteriosMovimento(int cdAit, String tpStatus) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		searchCriterios.addCriterios("A.tp_status", tpStatus, ItemComparator.NOTIN, Types.INTEGER);
		searchCriterios.addCriterios("A.lg_enviado_detran", String.valueOf(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey()), ItemComparator.NOTIN, Types.INTEGER);
		searchCriterios.addCriterios("A.tp_status",  this.movimentoNaoAtualizaStatusAit.getMovimento().toString().replace("[", "").replace("]", ""), ItemComparator.NOTIN, Types.INTEGER);
		return searchCriterios;
	}
}
