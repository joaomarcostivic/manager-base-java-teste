package com.tivic.manager.adapter.base.antiga.aitmovimento;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.text.BadLocationException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.util.richtext.ConversorRichText;

public class AdapterAitMovimento {
	public AitMovimentoOld adaptarToOld(AitMovimento aitMovimento) {
		return new AitMovimentoOldBuilder()
				.setCodAit(aitMovimento.getCdAit())
				.setNrMovimento(aitMovimento.getNrMovimento())
				.setDtMovimento(aitMovimento.getDtMovimento())
				.setNrRemessa(aitMovimento.getNrRemessa())
				.setTpStatus(aitMovimento.getTpStatus())
				.setTpArquivo(aitMovimento.getTpArquivo())
				.setCodOcorrencia(aitMovimento.getCdOcorrencia())
				.setLgEnviadoDetran(aitMovimento.getLgEnviadoDetran())
				.setStEntrega(aitMovimento.getStEntrega())
				.setNrProcesso(aitMovimento.getNrProcesso())
				.setDtRegistroDetran(aitMovimento.getDtRegistroDetran())
				.setStRecurso(aitMovimento.getStRecurso())
				.setNrSequencial(aitMovimento.getNrSequencial())
				.setNrErro(aitMovimento.getNrErro())
				.setDtDigitacao(aitMovimento.getDtDigitacao())
				.setLgCancelaMovimento(aitMovimento.getLgCancelaMovimento())
				.setDtCancelamento(aitMovimento.getDtCancelamento())
				.setNrRemessaRegistro(aitMovimento.getNrRemessaRegistro())
				.setDtPrimeiroRegistro(aitMovimento.getDtPrimeiroRegistro())
				.setStRegistroDetran(aitMovimento.getStRegistroDetran())
				.setCdProcesso(aitMovimento.getCdProcesso())
				.setCdUsuario(aitMovimento.getCdUsuario())
				.setCdContaReceber(aitMovimento.getCdContaReceber())
				.setDsObservacao(aitMovimento.getDsObservacao() != null ? aitMovimento.getDsObservacao().getBytes() : null)
				.build();
	}
	
	public AitMovimento adapterToBaseNova(AitMovimentoOld aitMovimentoOld) throws IOException, BadLocationException {
		return new AitMovimentoBuilder()
				.setCdAit(aitMovimentoOld.getCodigoAit())
				.setNrMovimento(aitMovimentoOld.getNrMovimento())
				.setDtMovimento(aitMovimentoOld.getDtMovimento())
				.setNrRemessa(aitMovimentoOld.getNrRemessa())
				.setTpStatus(aitMovimentoOld.getTpStatus())
				.setTpArquivo(aitMovimentoOld.getTpArquivo())
				.setCdOcorrencia(aitMovimentoOld.getCodOcorrencia())
				.setLgEnviadoDetran(aitMovimentoOld.getLgEnviadoDetran())
				.setStEntrega(aitMovimentoOld.getStEntrega())
				.setNrProcesso(aitMovimentoOld.getNrProcesso())
				.setDtRegistroDetran(aitMovimentoOld.getDtRegistroDetran())
				.setStRecurso(aitMovimentoOld.getStRecurso())
				.setNrSequencial(aitMovimentoOld.getNrSequencial())
				.setNrErro(aitMovimentoOld.getNrErro())
				.setDtDigitacao(aitMovimentoOld.getDtDigitacao())
				.setLgCancelaMovimento(aitMovimentoOld.getLgCancelaMovimento())
				.setDtCancelamento(aitMovimentoOld.getDtCancelamento())
				.setNrRemessaRegistro(aitMovimentoOld.getNrRemessaRegistro())
				.setDtPrimeiroRegistro(aitMovimentoOld.getDtPrimeiroRegistro())
				.setStRegistroDetran(aitMovimentoOld.getStRegistroDetran())
				.setCdProcesso(aitMovimentoOld.getCdProcesso())
				.setCdUsuario(aitMovimentoOld.getCdUsuario())
				.setCdContaReceber(aitMovimentoOld.getCdContaReceber())
				.setDsObservacao(converterDsObservacao(aitMovimentoOld.getDsObservacao()))
				.build();
	}
	
	private String converterDsObservacao(byte[] blbDsObservacao) throws IOException, BadLocationException {
		if (blbDsObservacao == null)
			return "";
		else 
			return new ConversorRichText().convert(new String(blbDsObservacao, StandardCharsets.ISO_8859_1));
	}
}
