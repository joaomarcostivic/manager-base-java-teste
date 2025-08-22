package com.tivic.manager.mob;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;

import sol.dao.ResultSetMap;

public class AitDTO extends Ait {
		
	private Infracao infracao;
	private AitMovimento movimentoAtual;
	private Equipamento equipamento;
	private Usuario usuario;
	private Agente agente;
	private Talonario talonario;
	
	public Infracao getInfracao() {
		return infracao;
	}

	public void setInfracao(Infracao infracao) {
		this.infracao = infracao;
	}
	

	public AitMovimento getMovimentoAtual() {
		return movimentoAtual;
	}

	public void setMovimentoAtual(AitMovimento movimentoAtual) {
		this.movimentoAtual = movimentoAtual;
	}


	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
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


	public static class ListBuilder {
		
		private ResultSetMapper<AitDTO> aits;
		private ResultSetMapper<Infracao> infracoes;
		private ResultSetMapper<AitMovimento> movimentosAtuais;
		private ResultSetMapper<Equipamento> equipamentos;
		private ResultSetMapper<Usuario> usuarios;
		private ResultSetMapper<Agente> agentes;
		private ResultSetMapper<Talonario> talonarios;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.aits = new ResultSetMapper<AitDTO>(rsm, AitDTO.class);
				setInfracao(rsm);
				setMovimentoAtual(rsm);
				setEquipamento(rsm);
				setUsuario(rsm);
				setAgente(rsm);
				setTalonario(rsm);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setInfracao(ResultSetMap rsm) {
			try {
				this.infracoes = new ResultSetMapper<Infracao>(rsm, Infracao.class);				
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setMovimentoAtual(ResultSetMap rsm) {
			try {
				this.movimentosAtuais = new ResultSetMapper<AitMovimento>(rsm, AitMovimento.class);				
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
		
		public ListBuilder setUsuario(ResultSetMap rsm) {
			try {
				this.usuarios = new ResultSetMapper<Usuario>(rsm, Usuario.class);				
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
		
		
		public List<AitDTO> build() throws IllegalArgumentException, Exception {
			
			List<AitDTO> aits = this.aits.toList();
			
			List<Infracao> infracoes = this.infracoes.toList();
			List<AitMovimento> movimentosAtuais = this.movimentosAtuais.toList();
			List<Equipamento> equipamentos = this.equipamentos.toList();
			List<Usuario> usuarios = this.usuarios.toList();
			List<Agente> agentes = this.agentes.toList();
			List<Talonario> talonarios = this.talonarios.toList();
			
			for(int i = 0; i < aits.size(); i++) {
				AitDTO dto = aits.get(i);
				
				if(i < infracoes.size() && dto.getCdInfracao()>0)
					dto.setInfracao(infracoes.get(i));
				
				if(i < movimentosAtuais.size() && dto.getCdMovimentoAtual()>0)
					dto.setMovimentoAtual(movimentosAtuais.get(i));
				
				if(i < equipamentos.size() && dto.getCdEquipamento()>0)
					dto.setEquipamento(equipamentos.get(i));
				
				if(i < usuarios.size() && dto.getCdUsuario()>0)
					dto.setUsuario(usuarios.get(i));
				
				if(i < agentes.size() && dto.getCdAgente()>0)
					dto.setAgente(agentes.get(i));

				if(i < talonarios.size() && dto.getCdTalao()>0)
					dto.setTalonario(talonarios.get(i));
			}
			
			return aits;
		}
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private AitDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), AitDTO.class);
				
				// TODO: imagens
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdAit, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Ait ait = AitDAO.get(cdAit);
				dto = objectMapper.readValue(ait.toString(), AitDTO.class);
				
				if(cascade) {
					
					if(ait.getCdInfracao()>0)
						setInfracao(ait.getCdInfracao(), cascade);
					
					if(ait.getCdMovimentoAtual()>0)
						setMovimentoAtual(ait.getCdMovimentoAtual(), ait.getCdAit(), cascade);
					
					if(ait.getCdEquipamento()>0)
						setEquipamento(ait.getCdEquipamento(), cascade);
					
					if(ait.getCdUsuario()>0)
						setUsuario(ait.getCdUsuario(), cascade);
					
					if(ait.getCdAgente()>0)
						setAgente(ait.getCdAgente(), cascade);
					
					if(ait.getCdTalao()>0)
						setTalonario(ait.getCdTalao(), cascade);
				}
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setInfracao(Map<String, Object> map) {
			try {
				dto.setInfracao(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Infracao.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setInfracao(Infracao infracao) {
			try {
				dto.setInfracao(infracao);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setInfracao(int cdInfracao, boolean cascade) {
			try {
				Infracao infracao = InfracaoDAO.get(cdInfracao);
				dto.setInfracao(infracao);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setMovimentoAtual(Map<String, Object> map) {
			try {
				dto.setMovimentoAtual(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), AitMovimento.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setMovimentoAtual(AitMovimento movimentoAtual) {
			try {
				dto.setMovimentoAtual(movimentoAtual);;
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		

		public Builder setMovimentoAtual(int cdMovimento, int cdAit, boolean cascade) {
			try {
				AitMovimento movimento = AitMovimentoDAO.get(cdMovimento, cdAit);
				dto.setMovimentoAtual(movimento);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setEquipamento(Map<String, Object> map) {
			try {
				dto.setEquipamento(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Equipamento.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setEquipamento(Equipamento equipamento) {
			try {
				dto.setEquipamento(equipamento);;
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		

		public Builder setEquipamento(int cdEquipamento, boolean cascade) {
			try {
				Equipamento equipamento = EquipamentoDAO.get(cdEquipamento);
				dto.setEquipamento(equipamento);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setUsuario(Map<String, Object> map) {
			try {
				dto.setUsuario(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Usuario.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setUsuario(Usuario usuario) {
			try {
				dto.setUsuario(usuario);;
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setUsuario(int cdUsuario, boolean cascade) {
			try {
				Usuario usuario = UsuarioDAO.get(cdUsuario);
				dto.setUsuario(usuario);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setAgente(Map<String, Object> map) {
			try {
				dto.setAgente(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Agente.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setAgente(Agente agente) {
			try {
				dto.setAgente(agente);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setAgente(int cdAgente, boolean cascade) {
			try {
				Agente agente = AgenteDAO.get(cdAgente);
				dto.setAgente(agente);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		

		public Builder setTalonario(Map<String, Object> map) {
			try {
				dto.setTalonario(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Talonario.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setTalonario(Talonario talonario) {
			try {
				dto.setTalonario(talonario);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setTalonario(int cdTalonario, boolean cascade) {
			try {
				Talonario talonario = TalonarioDAO.get(cdTalonario);
				dto.setTalonario(talonario);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		public AitDTO build() {
			return dto;
		}
		
	}


}
