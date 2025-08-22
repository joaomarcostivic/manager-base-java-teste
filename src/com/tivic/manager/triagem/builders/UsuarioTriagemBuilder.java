package com.tivic.manager.triagem.builders;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.triagem.dtos.ArquivoUsuarioTriagem;
import com.tivic.manager.triagem.dtos.UsuarioTriagemDTO;

public class UsuarioTriagemBuilder {
	
	private UsuarioTriagemDTO usuarioTriagemDTO;
	
	public UsuarioTriagemBuilder() {
		usuarioTriagemDTO = new UsuarioTriagemDTO();
	}
	
	public UsuarioTriagemBuilder pessoa(Pessoa pessoa) {
		usuarioTriagemDTO.setNmUsuario(pessoa.getNmPessoa());
		usuarioTriagemDTO.setNmEmail(pessoa.getNmEmail());
		usuarioTriagemDTO.setImagemPerfil(pegarImagemPerfil(pessoa));
		return this;
	}
	
	public UsuarioTriagemBuilder usuario(Usuario usuario) {
		usuarioTriagemDTO.setCdUsuario(usuario.getCdUsuario());
		usuarioTriagemDTO.setNmSenha(usuario.getNmSenha());
		usuarioTriagemDTO.setNmLogin(usuario.getNmLogin());
		usuarioTriagemDTO.setStUsuario(usuario.getStUsuario());
		return this;
	}
	
	private ArquivoUsuarioTriagem pegarImagemPerfil(Pessoa pessoa) {
		ArquivoUsuarioTriagem imagemPerfil = new ArquivoUsuarioTriagem();
		imagemPerfil.setCdArquivo(pessoa.getCdPessoa());
		imagemPerfil.setBlbArquivo(pessoa.getImgFoto());
		return imagemPerfil;
	}
	
	public UsuarioTriagemDTO build() {
		return usuarioTriagemDTO;
	}
	
}
