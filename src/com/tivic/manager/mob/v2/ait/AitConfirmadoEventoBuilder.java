package com.tivic.manager.mob.v2.ait;

import org.json.JSONObject;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoServices;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.StConsistenciaAitEnum;
import com.tivic.manager.util.Util;
import com.tivic.sol.util.date.DateUtil;

public class AitConfirmadoEventoBuilder {

	private Ait ait;
	
	public AitConfirmadoEventoBuilder() {
		ait = new Ait();
		ait.setDtDigitacao(DateUtil.getDataAtual());
		ait.setVlLatitude(0.0);
		ait.setVlLongitude(0.0);
		ait.setStAit(StConsistenciaAitEnum.ST_CONSISTENTE.getKey());
	}
	
	public AitConfirmadoEventoBuilder preencherComEventoEquipamento(EventoEquipamento eventoEquipamento) {
		this.ait.setCdEquipamento(eventoEquipamento.getCdEquipamento());
		this.ait.setDtInfracao(eventoEquipamento.getDtConclusao());
		this.ait.setVlVelocidadePermitida(Double.parseDouble(String.valueOf(eventoEquipamento.getVlLimite())));
		this.ait.setVlVelocidadePenalidade(Double.parseDouble(String.valueOf(eventoEquipamento.getVlConsiderada())));
		this.ait.setVlVelocidadeAferida(Double.parseDouble(String.valueOf(eventoEquipamento.getVlMedida())));
		return this;
	}
	
	public AitConfirmadoEventoBuilder preencherComEquipamento(Equipamento equipamento) {
		this.ait.setDtAfericao(equipamento.getDtAfericao());
		this.ait.setNrLacre(equipamento.getNrLacre());
		this.ait.setNrInventarioInmetro(equipamento.getNrInventarioInmetro());
		return this;
	}
	
	public AitConfirmadoEventoBuilder preencherComTalonario(Talonario talonario) {
		ait.setNrAit(talonario.getNrUltimoAit() + 1);
		String idTalao = talonario.getSgTalao() == null ? "" : talonario.getSgTalao();
		ait.setIdAit(idTalao + Util.fillNum(ait.getNrAit(), 10 - idTalao.length()));
		return this;
	}

	public AitConfirmadoEventoBuilder preencherComInformacoesVeiculo(InformacoesVeiculoDTO informacoesVeiculoDTO) throws Exception {
		if(informacoesVeiculoDTO != null) {
			if(informacoesVeiculoDTO.getCodMunicipio() != null 
			&& informacoesVeiculoDTO.getNmMunicipio() != null
			&& informacoesVeiculoDTO.getSgEstado() != null) {				
				int cdCidade = AitServices.buscarCdMunicipioVeiculo(
					informacoesVeiculoDTO.getCodMunicipio(), 
					informacoesVeiculoDTO.getNmMunicipio(), 
					informacoesVeiculoDTO.getSgEstado()
				);
				ait.setCdCidade(cdCidade);
			}
		}
		return this;
	}
	
	public AitConfirmadoEventoBuilder preencherComInformacoesAit(InformacoesAitDTO informacoesAitDTO) {
		this.ait.setDsLocalInfracao(informacoesAitDTO.getDsLocalInfracao());
		this.ait.setDsPontoReferencia(informacoesAitDTO.getDsPontoReferencia());
		this.ait.setCdAgente(informacoesAitDTO.getCdAgente());
		this.ait.setCdUsuario(informacoesAitDTO.getCdUsuarioConfirmacao());
		this.ait.setStAit(informacoesAitDTO.getStAit());
		this.ait.setNrPlaca(informacoesAitDTO.getNrPlaca());
		this.ait.setCdMarcaVeiculo(informacoesAitDTO.getCdMarcaAutuacao());
		this.ait.setCdMarcaAutuacao(informacoesAitDTO.getCdMarcaAutuacao());
		this.ait.setCdEspecieVeiculo(informacoesAitDTO.getCdEspecieVeiculo());
		this.ait.setCdTipoVeiculo(informacoesAitDTO.getCdTipoVeiculo());
		this.ait.setCdCidade(informacoesAitDTO.getCdCidade());
		this.ait.setCdCorVeiculo(informacoesAitDTO.getCdCorVeiculo());
		this.ait.setNmProprietario(informacoesAitDTO.getNmProprietario());
		this.ait.setNrAnoFabricacao(informacoesAitDTO.getNrAnoFabricacao().toString());
		this.ait.setNrAnoModelo(informacoesAitDTO.getNrAnoModelo().toString());
		this.ait.setDsObservacao(informacoesAitDTO.getDsObservacao());
		this.ait.setCdInfracao(informacoesAitDTO.getCdInfracao());
		Infracao infracao = InfracaoServices.getVigente(ait.getCdInfracao(), null);
		ait.setVlMulta(infracao != null && infracao.getVlInfracao() > 0 ? infracao.getVlInfracao() : null);
		return this;
	}
	
	public Ait build() {
		return ait; 
	}
	
}
