package com.tivic.manager.seg;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

@JsonIgnoreProperties(ignoreUnknown = false)
public class UsuarioDTO extends Usuario implements Serializable {
	private static final long serialVersionUID = 4649475921173992356L;
	
	private Pessoa pessoa;
	private Agente agente;
	
	public UsuarioDTO() {}

	public UsuarioDTO(int cdUsuario, int cdPessoa, int cdPerguntaSecreta, String nmLogin, String nmSenha, int tpUsuario,
			String nmRespostaSecreta, int stUsuario, String token) {
		super(cdUsuario, cdPessoa, cdPerguntaSecreta, nmLogin, nmSenha, tpUsuario, nmRespostaSecreta, stUsuario, token);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}	
	
	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	@Override
	public String toString() {
		String valueToString = "";
		valueToString += "cdUsuario: " +  getCdUsuario();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdPerguntaSecreta: " +  getCdPerguntaSecreta();
		valueToString += ", nmLogin: " +  getNmLogin();
		valueToString += ", nmSenha: " +  getNmSenha();
		valueToString += ", tpUsuario: " +  getTpUsuario();
		valueToString += ", nmRespostaSecreta: " +  getNmRespostaSecreta();
		valueToString += ", stUsuario: " +  getStUsuario();
		valueToString += ", token: " +  getToken();
		
		valueToString += ", pessoa: " + getPessoa();
		valueToString += ", agente: " + getAgente();
		
		return "{" + valueToString + "}";
	}
	
	/**
	 * Construir List<UsuarioDTO> a partir de um {@link ResultSetMap}	
	 */
	public static class ListBuilder {
		
		private ResultSetMapper<UsuarioDTO> rsmUsuario;
		private ResultSetMapper<Pessoa> rsmPessoa;
		private ResultSetMapper<Agente> rsmAgente;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.rsmUsuario = new ResultSetMapper<UsuarioDTO>(rsm, UsuarioDTO.class);	
				
				this.rsmPessoa = new ResultSetMapper<Pessoa>(new ResultSetMap(), Pessoa.class);	
				this.rsmAgente = new ResultSetMapper<Agente>(new ResultSetMap(), Agente.class);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public ListBuilder setPessoa(ResultSetMap rsm) {
			try {
				this.rsmPessoa = new ResultSetMapper<Pessoa>(rsm, Pessoa.class);	
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setAgente(ResultSetMap rsm) {
			try {
				this.rsmAgente = new ResultSetMapper<Agente>(rsm, Agente.class);		
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public List<UsuarioDTO> build() throws IllegalArgumentException, Exception {
			
			List<UsuarioDTO> usuarios = rsmUsuario.toList();
			List<Pessoa> pessoas = rsmPessoa.toList();
			List<Agente> agentes = rsmAgente.toList();
			
			for(int i = 0; i < usuarios.size(); i++) {
				UsuarioDTO dto = usuarios.get(i);
				dto.setNmSenha(null);
				dto.setNmRespostaSecreta(null);
				
				if(i < pessoas.size())
					dto.setPessoa(pessoas.get(i));
				
				if(i < agentes.size())
					dto.setAgente(agentes.get(i));
			}
			return usuarios;
		}
	}
	
	public static class Builder {
		
		private UsuarioDTO dto;
		private Gson gson;
		
		public Builder(Map<String, Object> map) {
			try {
				gson = new Gson();
				dto = gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), UsuarioDTO.class);
				dto.setNmSenha(null);
				dto.setNmRespostaSecreta(null);				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdUsuario, int cdPessoa, int cdPerguntaSecreta, String nmLogin, int tpUsuario,
				String nmRespostaSecreta, int stUsuario, String token) {
			dto = new UsuarioDTO(cdUsuario, cdPessoa, cdPerguntaSecreta, nmLogin, null, tpUsuario, nmRespostaSecreta, stUsuario, token);
		}
		
		public Builder(Usuario usuario) {
			dto = new UsuarioDTO(
					usuario.getCdUsuario(), 
					usuario.getCdPessoa(), 
					usuario.getCdPerguntaSecreta(), 
					usuario.getNmLogin(), 
					null, 
					usuario.getTpUsuario(), 
					usuario.getNmRespostaSecreta(), 
					usuario.getStUsuario(), 
					usuario.getToken());
		}
		
		public Builder setPessoa(Map<String, Object> map) {
			try {
				dto.setPessoa(gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), Pessoa.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setPessoa(Pessoa pessoa) {
			try {
				dto.setPessoa(pessoa);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setAgente(Map<String, Object> map) {
			try {
				dto.setAgente(gson.fromJson(Util.map2Json((HashMap<String, Object>)map).toString(), Agente.class));
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
		
		public Builder setStLogin(int stLogin) {
			try {
				dto.setStLogin(stLogin);
				return this;
			} catch (Exception e) {
				return null;
			}
		}
		
		public UsuarioDTO build() {
			return dto;
		}
		
	}
}
