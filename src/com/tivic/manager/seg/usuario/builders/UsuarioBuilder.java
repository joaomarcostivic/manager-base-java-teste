package com.tivic.manager.seg.usuario.builders;

import com.tivic.manager.seg.Usuario;

public class UsuarioBuilder {

	private Usuario usuario;

	public UsuarioBuilder() {
    	this.usuario = new Usuario();
    }

	public UsuarioBuilder setCdUsuario(int cdUsuario) {
		usuario.setCdUsuario(cdUsuario);
        return this;
    }

	public UsuarioBuilder setCdPessoa(int cdPessoa) {
		usuario.setCdPessoa(cdPessoa);
        return this;
    }

	public UsuarioBuilder setCdPerguntaSecreta(int cdPerguntaSecreta) {
		usuario.setCdPerguntaSecreta(cdPerguntaSecreta);
        return this;
    }

	public UsuarioBuilder setNmLogin(String nmLogin) {
		usuario.setNmLogin(nmLogin);
        return this;
    }

	public UsuarioBuilder setNmSenha(String nmSenha) {
		usuario.setNmSenha(nmSenha);
        return this;
    }

	public UsuarioBuilder setTpUsuario(int tpUsuario) {
		usuario.setTpUsuario(tpUsuario);
        return this;
    }

	public UsuarioBuilder setNmRespostaSecreta(String nmRespostaSecreta) {
		usuario.setNmRespostaSecreta(nmRespostaSecreta);
        return this;
    }

	public UsuarioBuilder setStUsuario(int stUsuario) {
		usuario.setStUsuario(stUsuario);
        return this;
    }

	public UsuarioBuilder setStLogin(int stLogin) {
		usuario.setStLogin(stLogin);
        return this;
    }

	public UsuarioBuilder setCdEquipamento(int cdEquipamento) {
		usuario.setCdEquipamento(cdEquipamento);
        return this;
    }

	public Usuario build() {
		return this.usuario;
	}

}