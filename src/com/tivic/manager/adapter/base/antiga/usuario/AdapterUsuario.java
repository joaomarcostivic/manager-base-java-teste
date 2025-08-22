package com.tivic.manager.adapter.base.antiga.usuario;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.usuario.builders.UsuarioAutenticacaoBuilder;

public class AdapterUsuario {
	
	public UsuarioOld toBaseAntiga(Usuario usuario) {
		return new UsuarioAutenticacaoOldBuilder().usuario(usuario).build();
	}
	
	public Usuario toBaseNova(UsuarioOld usuarioOld) throws IOException, BadLocationException {
		return new UsuarioAutenticacaoBuilder().usuario(usuarioOld).build();
	}

}
