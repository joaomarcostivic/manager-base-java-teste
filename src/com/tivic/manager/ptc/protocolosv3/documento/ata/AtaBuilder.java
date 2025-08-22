package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.GregorianCalendar;

import com.tivic.manager.util.DateUtil;

public class AtaBuilder {
	private Ata ata;
	
	public AtaBuilder() {
		ata = new Ata();
		ata.setDtCadastro(DateUtil.today());
	}
	
	public AtaBuilder addCdAta(int cdAta) {
		ata.setCdAta(cdAta);
		return this;
	}
	
	public AtaBuilder addCdUsuario(int cdUsuario) {
		ata.setCdUsuario(cdUsuario);
		return this;
	}
	
	public AtaBuilder addDtAta(GregorianCalendar dtAta) {
		ata.setDtAta(dtAta);
		return this;
	}
	
	public AtaBuilder addIdAta(String idAta) {
		ata.setIdAta(idAta);
		return this;
	}
	
	public Ata build() {
		return ata;
	}
}
