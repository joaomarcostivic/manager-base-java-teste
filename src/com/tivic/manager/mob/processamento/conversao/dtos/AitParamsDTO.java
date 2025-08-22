package com.tivic.manager.mob.processamento.conversao.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.util.detran.VeiculoDTO;

public class AitParamsDTO {
	private EventoEquipamento evento;
	private Infracao infracao;
	private Equipamento equipamento;
	private VeiculoDTO veiculo;
	private int cdUsuario;
	private Talonario talao;
	private Orgao orgao;
	private TipoEvento tipoEvento;
	private int cdAgente;
	private int tpConvenio;

	public AitParamsDTO() { }
	
	public EventoEquipamento getEvento() {
		return evento;
	}

	public void setEvento(EventoEquipamento evento) {
		this.evento = evento;
	}
	
	public Infracao getInfracao() {
		return infracao;
	}

	public void setInfracao(Infracao infracao) {
		this.infracao = infracao;
	}
	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public VeiculoDTO getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(VeiculoDTO veiculo) {
		this.veiculo = veiculo;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}
	
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	
	public Talonario getTalao() {
		return talao;
	}

	public void setTalao(Talonario talao) {
		this.talao = talao;
	}

	public Orgao getOrgao() {
		return orgao;
	}

	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	
	public int getCdAgente() {
		return cdAgente;
	}
	
	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}

	public int getTpConvenio() {
		return tpConvenio;
	}

	public void setTpConvenio(int tpConvenio) {
		this.tpConvenio = tpConvenio;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}

}