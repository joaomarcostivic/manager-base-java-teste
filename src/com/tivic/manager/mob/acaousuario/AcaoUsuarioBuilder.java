package com.tivic.manager.mob.acaousuario;

import java.util.GregorianCalendar;

public class AcaoUsuarioBuilder {
	
	private AcaoUsuario acaoUsuario;
	
	public AcaoUsuarioBuilder(AcaoUsuarioDTO acaoUsuarioDTO) {
		acaoUsuario = new AcaoUsuario();
		GregorianCalendar dtAcao = new GregorianCalendar();
		setAcaoUsuario(acaoUsuarioDTO, dtAcao);
	}
	
	private void setAcaoUsuario(AcaoUsuarioDTO acaoUsuarioDTO, GregorianCalendar dtAcao) {	
		dtAcao.setTimeInMillis(acaoUsuarioDTO.getDtAcao());
		this.acaoUsuario.setCdAcao(acaoUsuarioDTO.getCdAcao());
		this.acaoUsuario.setCdUsuario(acaoUsuarioDTO.getCdUsuario());
		this.acaoUsuario.setDsAcao(acaoUsuarioDTO.getDsAcao());
		this.acaoUsuario.setDtAcao(dtAcao);
	}
	
	public AcaoUsuario build() {
		return this.acaoUsuario;
	}
}
