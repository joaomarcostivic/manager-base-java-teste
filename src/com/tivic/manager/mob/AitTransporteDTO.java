package com.tivic.manager.mob;

import java.sql.SQLException;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.util.ResultSetMapper;
import sol.dao.ResultSetMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AitTransporteDTO extends AitTransporte {

	private InfracaoTransporte infracaoTransporte;
	private com.tivic.manager.grl.Equipamento equipamento;
	private Usuario usuario;
	private Pessoa concessionario;
	private ConcessaoVeiculo concessaoVeiculo;
	private Agente agente;
	private Talonario talonario;

	public InfracaoTransporte getInfracaoTransporte() {
		return infracaoTransporte;
	}

	public void setInfracaoTransporte(InfracaoTransporte infracaoTransporte) {
		this.infracaoTransporte = infracaoTransporte;
	}

	public com.tivic.manager.grl.Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(com.tivic.manager.grl.Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Talonario getTalonario() {
		return talonario;
	}

	public void setTalonario(Talonario talonario) {
		this.talonario = talonario;
	}

	public Pessoa getConcessionario() {
		return concessionario;
	}

	public void setConcessionario(Pessoa concessionario) {
		this.concessionario = concessionario;
	}

	public ConcessaoVeiculo getConcessaoVeiculo() {
		return concessaoVeiculo;
	}

	public void setConcessaoVeiculo(ConcessaoVeiculo concessaoVeiculo) {
		this.concessaoVeiculo = concessaoVeiculo;
	}

 	public static class ListBuilder {

		private ResultSetMapper<AitTransporteDTO> aits;
		private ResultSetMapper<InfracaoTransporte> infracoesTransporte;
		private ResultSetMapper<Equipamento> equipamentos;
		private ResultSetMapper<Pessoa> concessionario;
		private ResultSetMapper<Agente> agentes;
		private ResultSetMapper<Talonario> talonarios;

		public ListBuilder(ResultSetMap rsm) {
			try {
				this.aits = new ResultSetMapper<AitTransporteDTO>(rsm, AitTransporteDTO.class);
				setInfracaoTransporte(rsm);
				setEquipamento(rsm);
				setConcessionario(rsm);
				setAgente(rsm);
				setTalonario(rsm);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setInfracaoTransporte(ResultSetMap rsm) {
			try {
				this.infracoesTransporte = new ResultSetMapper<InfracaoTransporte>(rsm, InfracaoTransporte.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public ListBuilder setEquipamento(ResultSetMap rsm) {
			try {
				this.equipamentos = new ResultSetMapper<Equipamento>(rsm, Equipamento.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public ListBuilder setConcessionario(ResultSetMap rsm) {
			try {
				this.concessionario = new ResultSetMapper<Pessoa>(rsm, Pessoa.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public ListBuilder setAgente(ResultSetMap rsm) {
			try {
				this.agentes = new ResultSetMapper<Agente>(rsm, Agente.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public ListBuilder setTalonario(ResultSetMap rsm) {
			try {
				this.talonarios = new ResultSetMapper<Talonario>(rsm, Talonario.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<AitTransporteDTO> build() throws IllegalArgumentException, Exception {

			List<AitTransporteDTO> aits = this.aits.toList();

			List<InfracaoTransporte> infracoesTransporte = this.infracoesTransporte.toList();
			List<Equipamento> equipamentos = this.equipamentos.toList();
			List<Pessoa> concessionario = this.concessionario.toList();
			List<Agente> agentes = this.agentes.toList();
			List<Talonario> talonarios = this.talonarios.toList();

			for (int i = 0; i < aits.size(); i++) {
				AitTransporteDTO dto = aits.get(i);

				if (i < infracoesTransporte.size() && dto.getCdInfracao() > 0)
					dto.setInfracaoTransporte(infracoesTransporte.get(i));

				if (i < equipamentos.size() && dto.getCdEquipamento() > 0)
					dto.setEquipamento(equipamentos.get(i));

				if (i < concessionario.size())
					dto.setConcessionario(concessionario.get(i));

				if (i < agentes.size() && dto.getCdAgente() > 0)
					dto.setAgente(agentes.get(i));

				if (i < talonarios.size() && dto.getCdTalao() > 0)
					dto.setTalonario(talonarios.get(i));
			}

			return aits;
		}
	}

}