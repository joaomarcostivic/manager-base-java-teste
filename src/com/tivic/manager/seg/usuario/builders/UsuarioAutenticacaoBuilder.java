package com.tivic.manager.seg.usuario.builders;

import com.tivic.manager.adapter.base.antiga.usuario.UsuarioOld;
import com.tivic.manager.seg.Usuario;

public class UsuarioAutenticacaoBuilder {
	
	private int cdUsuario;
	private int cdPessoa;
	private String nmLogin;
	private String nmSenha;
	private int cdPerguntaSecreta;
	private int tpUsuario;
	private String nmRespostaSecreta;
	private int stUsuario;
	private int stLogin;
	private int cdEquipamento;

	public UsuarioAutenticacaoBuilder usuario(UsuarioOld usuarioOld) {
        this.cdUsuario = usuarioOld.getCodUsuario();
        this.cdPessoa = usuarioOld.getCdPessoa();
        this.nmLogin = usuarioOld.getNmNick();
        this.cdPerguntaSecreta = 0;
        this.tpUsuario = usuarioOld.getNrNivel() == 0 ? 0 : 4;
        this.nmSenha = usuarioOld.getNmSenha();
        this.stUsuario = usuarioOld.getStUsuario();
        this.nmRespostaSecreta = null;
        this.stLogin = usuarioOld.getStLogin();
        this.cdEquipamento = usuarioOld.getCdEquipamento();
        return this;
    }
	
	public Usuario build() {
		return new Usuario(cdUsuario, cdPessoa, nmLogin, cdPerguntaSecreta, tpUsuario, nmSenha, stUsuario, nmRespostaSecreta, stLogin, cdEquipamento);
	}
}
