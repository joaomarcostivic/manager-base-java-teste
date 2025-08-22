package com.tivic.manager.ptc.tipodocumento;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.tipodocumento.tipostatusdisponiveis.ProtocoloStatusLancados;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class CircuitoAitProtocoloFilter {
	private List<Integer> itensPossiveis;
	private List<Integer> itensFiltrados;
	private List<Integer> tpStatusProtocolos;
	private AitMovimentoRepository movimentoRepository;
	private int cdAit;
	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	private DocumentoRepository documentoRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoServices;

	public CircuitoAitProtocoloFilter(List<Integer> itensPossiveis, int cdAit) throws Exception {
		this.movimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.itensPossiveis = new ArrayList<Integer>(itensPossiveis);
		this.itensFiltrados = new ArrayList<Integer>();
		this.cdAit = cdAit;
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		adicionarTiposProtocolos();
	}
	
	public List<Integer> filtrar() throws Exception{
		List<Integer> tpProtocolosLancados = getProtocolosLancados();
		verificarDisponibiladeJari(this.cdAit);
		for (Integer itemPossivel : itensPossiveis) {
			if(tpStatusProtocolos.contains(itemPossivel) && verificarItemPossivel(itemPossivel, tpProtocolosLancados)) {
				this.itensFiltrados.add(itemPossivel);
			}
		}
		return this.itensFiltrados;
	}
	
	private void verificarDisponibiladeJari(int cdAit) throws Exception {
		Ait ait = this.aitRepository.get(cdAit);
		if(ait.getTpStatus() != TipoStatusEnum.DEFESA_INDEFERIDA.getKey())
			return;
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.NIP_ENVIADA.getKey());
		if(aitMovimento.getCdMovimento() > 0)
			this.itensFiltrados.add(TipoStatusEnum.RECURSO_JARI.getKey());
	}
	
	private void adicionarTiposProtocolos() {
		this.tpStatusProtocolos = new ArrayList<Integer>();
		this.tpStatusProtocolos.add(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		this.tpStatusProtocolos.add(TipoStatusEnum.DEFESA_PREVIA.getKey());
		this.tpStatusProtocolos.add(TipoStatusEnum.RECURSO_JARI.getKey());
		this.tpStatusProtocolos.add(TipoStatusEnum.RECURSO_CETRAN.getKey());
		this.tpStatusProtocolos.add(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey());
	}
	
	private boolean verificarItemPossivel(int itemPossivel, List<Integer> tpProtocolosLancados) throws Exception {
		if (tpProtocolosLancados.contains(itemPossivel)) {
			List<AitMovimento> aitMovimentoList = getMovimentos(this.cdAit, itemPossivel);
			for (AitMovimento aitMovimento : aitMovimentoList) {
				SearchCriterios criterios = criteriosDocumentoMovimento(aitMovimento);
				List<AitMovimentoDocumento> aitMovimentoDocumentoList = this.aitMovimentoDocumentoRepository.find(criterios);
				Documento documento = this.documentoRepository.get(aitMovimentoDocumentoList.get(0).getCdDocumento());
				int codigoCancelamento = ParametroServices.getValorOfParametroAsInteger("CD_FASE_CANCELADO", 0);
				if (documento.getCdFase() == codigoCancelamento) {
					continue;
				} else {
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}
	
	private List<AitMovimento> getMovimentos(int cdAit, int itemPossivel) throws Exception {
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > 0);
		criterios.addCriteriosEqualInteger("tp_status", itemPossivel, true);
		return this.aitMovimentoRepository.find(criterios);
	}
	
	private SearchCriterios criteriosDocumentoMovimento(AitMovimento aitMovimento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitMovimento.getCdAit());
		searchCriterios.addCriteriosEqualInteger("cd_movimento", aitMovimento.getCdMovimento());
		return searchCriterios;
	}
	
	private List<Integer> getProtocolosLancados() throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriterios("tp_status", this.tpStatusProtocolos.toString().replace("[", "").replace("]", ""), ItemComparator.IN, Types.INTEGER);
		List<AitMovimento> protocolosLancados = movimentoRepository.find(searchCriterios);
		List<Integer> lancados = new ProtocoloStatusLancados(protocolosLancados).getStatusLancados();
		return lancados;
	}
	
	
}
