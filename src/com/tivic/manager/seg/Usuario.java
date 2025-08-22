package com.tivic.manager.seg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {
	private int cdUsuario;
	private int cdPessoa;
	private int cdPerguntaSecreta;
	private String nmLogin;
	private String nmSenha;
	private int tpUsuario;
	private String nmRespostaSecreta;
	private int stUsuario;
	private int stLogin;
	private int cdEquipamento;
	private String token;
	public Usuario(){ }
	public Usuario(int cdUsuario,
			int cdPessoa,
			int cdPerguntaSecreta,
			String nmLogin,
			String nmSenha,
			int tpUsuario,
			String nmRespostaSecreta,
			int stUsuario){
		setCdUsuario(cdUsuario);
		setCdPessoa(cdPessoa);
		setCdPerguntaSecreta(cdPerguntaSecreta);
		setNmLogin(nmLogin);
		setNmSenha(nmSenha);
		setTpUsuario(tpUsuario);
		setNmRespostaSecreta(nmRespostaSecreta);
		setStUsuario(stUsuario);
	}
	public Usuario(int cdUsuario,
			int cdPessoa,
			int cdPerguntaSecreta,
			String nmLogin,
			String nmSenha,
			int tpUsuario,
			String nmRespostaSecreta,
			int stUsuario,
			String token){
		setCdUsuario(cdUsuario);
		setCdPessoa(cdPessoa);
		setCdPerguntaSecreta(cdPerguntaSecreta);
		setNmLogin(nmLogin);
		setNmSenha(nmSenha);
		setTpUsuario(tpUsuario);
		setNmRespostaSecreta(nmRespostaSecreta);
		setStUsuario(stUsuario);
		setToken(token);
	}
	public Usuario(int cdUsuario,
			int cdPessoa,
			String nmLogin,
			int cdPerguntaSecreta,
			int tpUsuario,
			String nmSenha,
			int stUsuario,
			String nmRespostaSecreta,
			int stLogin,
			int cdEquipamento){
		setCdUsuario(cdUsuario);
		setCdPessoa(cdPessoa);
		setNmLogin(nmLogin);
		setCdPerguntaSecreta(cdPerguntaSecreta);
		setTpUsuario(tpUsuario);
		setNmSenha(nmSenha);
		setStUsuario(stUsuario);
		setNmRespostaSecreta(nmRespostaSecreta);
		setStLogin(stLogin);
		setCdEquipamento(cdEquipamento);
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdPerguntaSecreta(int cdPerguntaSecreta){
		this.cdPerguntaSecreta=cdPerguntaSecreta;
	}
	public int getCdPerguntaSecreta(){
		return this.cdPerguntaSecreta;
	}
	public void setNmLogin(String nmLogin){
		this.nmLogin=nmLogin;
	}
	public String getNmLogin(){
		return this.nmLogin;
	}
	public void setNmSenha(String nmSenha){
		this.nmSenha=nmSenha;
	}
	public String getNmSenha(){
		return this.nmSenha;
	}
	public void setTpUsuario(int tpUsuario){
		this.tpUsuario=tpUsuario;
	}
	public int getTpUsuario(){
		return this.tpUsuario;
	}
	public void setNmRespostaSecreta(String nmRespostaSecreta){
		this.nmRespostaSecreta=nmRespostaSecreta;
	}
	public String getNmRespostaSecreta(){
		return this.nmRespostaSecreta;
	}
	public void setStUsuario(int stUsuario){
		this.stUsuario=stUsuario;
	}
	public int getStUsuario(){
		return this.stUsuario;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	public void setStLogin (int stLogin) {
		this.stLogin = stLogin;
	}
	public int getStLogin () {
		return stLogin;
	}
	public String toString() {
		String valueToString = "";
		valueToString += " \"cdUsuario\": " +  getCdUsuario();
		valueToString += ", \"cdPessoa\": " +  getCdPessoa();
		valueToString += ", \"cdPerguntaSecreta\": " +  getCdPerguntaSecreta();
		valueToString += ", \"nmLogin\": \"" +  getNmLogin()+"\"";
		valueToString += ", \"nmSenha\": \"" +  getNmSenha()+"\"";
		valueToString += ", \"tpUsuario\": " +  getTpUsuario();
		valueToString += ", \"nmRespostaSecreta\": \"" +  getNmRespostaSecreta()+"\"";
		valueToString += ", \"stUsuario\": " +  getStUsuario();
		valueToString += ", \"stLogin\": " + getStLogin();
		valueToString += ", \"cdEquipamento\": " + getCdEquipamento();
		valueToString += ", \"token\": \"" +  getToken()+"\"";
		return "{" + valueToString + "}";
	}
	public Object clone() {
		return new Usuario(getCdUsuario(),
			getCdPessoa(),
			getCdPerguntaSecreta(),
			getNmLogin(),
			getNmSenha(),
			getTpUsuario(),
			getNmRespostaSecreta(),
			getStUsuario(),
			getToken());
	}
	private Usuario(Builder builder) {
		new Usuario(
				builder.getCdUsuario(),
				builder.getCdPessoa(),
				builder.getCdPerguntaSecreta(),
				builder.getNmLogin(),
				builder.getNmSenha(),
				builder.getTpUsuario(),
				builder.getNmRespostaSecreta(),
				builder.getStUsuario(),
				builder.getToken());
	}
	public int getCdEquipamento() {
		return cdEquipamento;
	}
	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}
	public static class Builder {
		private int cdUsuario;
		private String nmLogin;
		private String nmSenha;		
		private int cdPessoa;
		private int cdPerguntaSecreta;
		private int tpUsuario;
		private String nmRespostaSecreta;
		private int stUsuario;
		private String token;
		public Builder(int cdUsuario, String nmLogin) {
			this.setCdUsuario(cdUsuario);
			this.setNmLogin(nmLogin);
		}
		public Usuario build() {
			return new Usuario(this);
		}
		public int getCdUsuario() {
			return cdUsuario;
		}
		public Builder setCdUsuario(int cdUsuario) {
			this.cdUsuario = cdUsuario;
			return this;
		}
		public String getNmLogin() {
			return nmLogin;
		}
		public Builder setNmLogin(String nmLogin) {
			this.nmLogin = nmLogin;
			return this;
		}
		public String getNmSenha() {
			return nmSenha;
		}
		public Builder setNmSenha(String nmSenha) {
			this.nmSenha = nmSenha;
			return this;
		}
		public int getCdPessoa() {
			return cdPessoa;
		}
		public Builder setCdPessoa(int cdPessoa) {
			this.cdPessoa = cdPessoa;
			return this;
		}
		public int getCdPerguntaSecreta() {
			return cdPerguntaSecreta;
		}
		public Builder setCdPerguntaSecreta(int cdPerguntaSecreta) {
			this.cdPerguntaSecreta = cdPerguntaSecreta;
			return this;
		}
		public int getTpUsuario() {
			return tpUsuario;
		}
		public Builder setTpUsuario(int tpUsuario) {
			this.tpUsuario = tpUsuario;
			return this;
		}
		public String getNmRespostaSecreta() {
			return nmRespostaSecreta;
		}
		public Builder setNmRespostaSecreta(String nmRespostaSecreta) {
			this.nmRespostaSecreta = nmRespostaSecreta;
			return this;
		}
		public int getStUsuario() {
			return stUsuario;
		}
		public Builder setStUsuario(int stUsuario) {
			this.stUsuario = stUsuario;
			return this;
		}
		public String getToken() {
			return token;
		}
		public Builder setToken(String token) {
			this.token = token;
			return this;
		}
	}
	public enum Tipo {
		ADMINISTRADOR(0), COMUM(1), CORRESPONDENTE(2), CONSULTA(3);
		private final int value;
		private Tipo(int value) {
			this.value = value;
		}
		public int getValue() {
			return this.value;
		}
	}
}