package com.tivic.manager.adapter.base.antiga.infracao;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.infracao.InfracaoBuilder;

public class AdapterInfracao {
	public InfracaoOld toBaseAntiga(Infracao infracao) {
		return new InfracaoOldBuilder()
				.setCodInfracao(infracao.getCdInfracao())
				.setDsInfracao(infracao.getDsInfracao())
				.setNrPontuacao(infracao.getNrPontuacao())
				.setNrCodDetran(infracao.getNrCodDetran())
				.setNrValorUfir(infracao.getVlUfir())
				.setNmNatureza(infracao.getNmNatureza())
				.setNrArtigo(infracao.getNrArtigo())
				.setNrParagrafo(infracao.getNrParagrafo())
				.setNrInciso(infracao.getNrInciso())
				.setNrAlinea(infracao.getNrAlinea())
				.setTpCompetencia(infracao.getTpCompetencia())
				.setLgPrioritaria(infracao.getLgPrioritaria())
				.setDtFimVigencia(infracao.getDtFimVigencia())
				.setVlInfracao(infracao.getVlInfracao())
				.setLgSuspensaoCnh(infracao.getLgSuspensaoCnh())
				.setTpResponsabilidade(infracao.getTpResponsabilidade())
				.build();
	}
	
	public Infracao toBaseNova(InfracaoOld infracaoOld) throws IOException, BadLocationException {
		return new InfracaoBuilder()
				.setCdInfracao(infracaoOld.getCodInfracao())
				.setDsInfracao(infracaoOld.getDsInfracao2())
				.setNrPontuacao(infracaoOld.getNrPontuacao())
				.setNrCodDetran(infracaoOld.getNrCodDetran())
				.setVlUfir(infracaoOld.getNrValorUfir())
				.setNmNatureza(infracaoOld.getNmNatureza())
				.setNrArtigo(infracaoOld.getNrArtigo())
				.setNrParagrafo(infracaoOld.getNrParagrafo())
				.setNrInciso(infracaoOld.getNrInciso())
				.setNrAlinea(infracaoOld.getNrAlinea())
				.setTpCompetencia(infracaoOld.getTpCompetencia())
				.setLgPrioritaria(infracaoOld.getLgPrioritaria())
				.setDtFimVigencia(infracaoOld.getDtFimVigencia())
				.setVlInfracao(infracaoOld.getVlInfracao())
				.setLgSuspensaoCnh(infracaoOld.getLgSuspensaoCnh())
				.setTpResponsabilidade(infracaoOld.getTpResponsabilidade())
				.build();
	}
}
