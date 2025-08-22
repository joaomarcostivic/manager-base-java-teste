package com.tivic.manager.adapter.base.antiga.usuario;

public class UsuarioOldBuilder {
	private UsuarioOld usuarioOld;
	
	public UsuarioOldBuilder() {
    	this.usuarioOld = new UsuarioOld();
    }
	
	public UsuarioOldBuilder setCodUsuario(int codUsuario) {
		usuarioOld.setCodUsuario(codUsuario);
        return this;
    }
	
	public UsuarioOldBuilder setNmNick(String nmNick) {
		usuarioOld.setNmNick(nmNick);
        return this;
    }
	
	public UsuarioOldBuilder setNmUsuario(String nmUsuario) {
		usuarioOld.setNmUsuario(nmUsuario);
        return this;
    }
	
	public UsuarioOldBuilder setNrNivel(int nrNivel) {
		usuarioOld.setNrNivel(nrNivel);
        return this;
    }
	
	public UsuarioOldBuilder setNmSenha(String nmSenha) {
		usuarioOld.setNmSenha(nmSenha);
        return this;
    }
	
	public UsuarioOldBuilder setStUsuario(int stUsuario) {
		usuarioOld.setStUsuario(stUsuario);
        return this;
    }
	
	public UsuarioOldBuilder setStLogin(int stLogin) {
		usuarioOld.setStLogin(stLogin);
        return this;
    }
	
	public UsuarioOldBuilder setCdEquipamento(int cdEquipamento) {
		usuarioOld.setCdEquipamento(cdEquipamento);
        return this;
    }
	
	public UsuarioOld build() {
		return this.usuarioOld;
	}
	
}
