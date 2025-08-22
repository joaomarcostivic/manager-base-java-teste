package com.tivic.manager.adapter.base.antiga.talonario;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.talonario.TalonarioBuilder;

public class AdapterTalonario {
	public TalonarioOld toBaseAntiga(Talonario talonario) {
		return new TalonarioOldBuilder()
				.setCodTalao(talonario.getCdTalao())
				.setNrTalao(talonario.getNrTalao())
				.setNrInicial(talonario.getNrInicial())
				.setNrFinal(talonario.getNrFinal())
				.setCodAgente(talonario.getCdAgente())
				.setDtEntrega(talonario.getDtEntrega())
				.setDtDevolucao(talonario.getDtDevolucao())
				.setStTalao(talonario.getStTalao())
				.setTpTalao(talonario.getTpTalao())
				.setSgTalao(talonario.getSgTalao())
				.setNrUltimoAit(talonario.getNrUltimoAit())
				.build();
	}
	
	public Talonario toBaseNova(TalonarioOld talonarioOld) throws IOException, BadLocationException {
		return new TalonarioBuilder()
				.setCdTalao(talonarioOld.getCodTalao())
				.setNrTalao(talonarioOld.getNrTalao())
				.setNrInicial(talonarioOld.getNrInicial())
				.setNrFinal(talonarioOld.getNrFinal())
				.setCdAgente(talonarioOld.getCodAgente())
				.setDtEntrega(talonarioOld.getDtEntrega())
				.setDtDevolucao(talonarioOld.getDtDevolucao())
				.setStTalao(talonarioOld.getStTalao())
				.setTpTalao(talonarioOld.getTpTalao())
				.setSgTalao(talonarioOld.getSgTalao())
				.setNrUltimoAit(talonarioOld.getNrUltimoAit())
				.build();
	}
}
