package com.tivic.manager.adapter.base.antiga.usuario;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsuarioOld {
	private int codUsuario;
	private String nmNick;
	private String nmUsuario;
	private int nrNivel;
	private String nmSenha;
	private int stUsuario;
	private int stLogin;
	private int cdEquipamento;
	private int cdPessoa;
	
	public UsuarioOld() {}
	
	public UsuarioOld(
			int codUsuario,
			String nmNick,
			String nmUsuario,
			int nrNivel,
			String nmSenha,
			int stUsuario,
			int stLogin,
			int cdEquipamento,
			int cdPessoa) {
		setCodUsuario(codUsuario);
		setNmNick(nmNick);
		setNmUsuario(nmUsuario);
		setNrNivel(nrNivel);
		setNmSenha(nmSenha);
		setStUsuario(stUsuario);
		setStLogin(stLogin);
		setCdEquipamento(cdEquipamento);
		setCdPessoa(cdPessoa);
	}

	public int getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(int codUsuario) {
		this.codUsuario = codUsuario;
	}

	public String getNmNick() {
		return nmNick;
	}

	public void setNmNick(String nmNick) {
		this.nmNick = nmNick;
	}

	public String getNmUsuario() {
		return nmUsuario;
	}

	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

	public int getNrNivel() {
		return nrNivel;
	}

	public void setNrNivel(int nrNivel) {
		this.nrNivel = nrNivel;
	}

	public String getNmSenha() {
		return nmSenha;
	}

	public void setNmSenha(String nmSenha) {
		this.nmSenha = nmSenha;
	}

	public int getStUsuario() {
		return stUsuario;
	}

	public void setStUsuario(int stUsuario) {
		this.stUsuario = stUsuario;
	}

	public int getStLogin() {
		return stLogin;
	}

	public void setStLogin(int stLogin) {
		this.stLogin = stLogin;
	}

	public int getCdEquipamento() {
		return cdEquipamento;
	}

	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}

	public int getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
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
