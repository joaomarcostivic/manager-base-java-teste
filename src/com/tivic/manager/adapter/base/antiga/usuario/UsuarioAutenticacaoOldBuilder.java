package com.tivic.manager.adapter.base.antiga.usuario;

import com.tivic.manager.seg.Usuario;

public class UsuarioAutenticacaoOldBuilder {

    private int codUsuario;
    private int cdPessoa;
    private String nmNick;
    private String nmUsuario;
    private int nrNivel;
    private String nmSenha;
    private int stUsuario;
    private int stLogin;
    private int cdEquipamento;
    
    public UsuarioAutenticacaoOldBuilder usuario(Usuario usuario) {
        this.codUsuario = usuario.getCdUsuario();
        this.cdPessoa = usuario.getCdPessoa();
        this.nmNick = usuario.getNmLogin();
        this.nmUsuario = null;
        this.nrNivel = 0;
        this.nmSenha = usuario.getNmSenha();
        this.stUsuario = usuario.getStUsuario();
        this.stLogin = usuario.getStLogin();
        this.cdEquipamento = usuario.getCdEquipamento();
        return this;
    }

    public UsuarioOld build() {
        return new UsuarioOld(codUsuario, nmNick, nmUsuario, nrNivel, nmSenha, stUsuario, stLogin, cdEquipamento, cdPessoa);
    }
}
