package com.tivic.manager.ptc.portal.vagaespecial;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArquivoDTO {
	private String nmTipoArquivo;
	private int cdArquivo;
	private String nmArquivo;
	private GregorianCalendar dtArquivamento;
	private String nmDocumento;
	private int nrRegistros;
	private byte[] blbArquivo;
	private int cdTipoArquivo;
	private GregorianCalendar dtCriacao;
	private int cdUsuario;
	private int stArquivo;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int cdAssinatura;
	private String txtOcr;
	private String txtCaminhoArquivo;
	private String dsArquivo;
	
	public ArquivoDTO() { }

	public ArquivoDTO(String nmTipoArquivo, 
			int cdArquivo,
			String nmArquivo,
			GregorianCalendar dtArquivamento,
			String nmDocumento,
			byte[] blbArquivo,
			int cdTipoArquivo,
			GregorianCalendar dtCriacao,
			int cdUsuario,
			int stArquivo,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int cdAssinatura,
			String txtOcr,
			int nrRegistro,
			String txtCaminhoArquivo,
			String dsArquivo) {
		setnmTipoArquivo(nmTipoArquivo);
		setCdArquivo(cdArquivo);
		setNmArquivo(nmArquivo);
		setDtArquivamento(dtArquivamento);
		setNmDocumento(nmDocumento);
		setBlbArquivo(blbArquivo);
		setCdTipoArquivo(cdTipoArquivo);
		setDtCriacao(dtCriacao);
		setCdUsuario(cdUsuario);
		setStArquivo(stArquivo);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setCdAssinatura(cdAssinatura);
		setTxtOcr(txtOcr);
		setNrRegistros(nrRegistro);
		setTxtCaminhoArquivo(txtCaminhoArquivo);
		setDsArquivo(dsArquivo);
	}
	
	public String getnmTipoArquivo() {
		return nmTipoArquivo;
	}

	public void setnmTipoArquivo(String nmTipoArquivo) {
		this.nmTipoArquivo = nmTipoArquivo;
	}

	public int getCdArquivo() {
		return cdArquivo;
	}

	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public GregorianCalendar getDtArquivamento() {
		return dtArquivamento;
	}

	public void setDtArquivamento(GregorianCalendar dtArquivamento) {
		this.dtArquivamento = dtArquivamento;
	}

	public String getNmDocumento() {
		return nmDocumento;
	}

	public void setNmDocumento(String nmDocumento) {
		this.nmDocumento = nmDocumento;
	}

	public int getNrRegistros() {
		return nrRegistros;
	}

	public void setNrRegistros(int nrRegistros) {
		this.nrRegistros = nrRegistros;
	}

	public byte[] getBlbArquivo() {
		return blbArquivo;
	}

	public void setBlbArquivo(byte[] blbArquivo) {
		this.blbArquivo = blbArquivo;
	}

	public int getCdTipoArquivo() {
		return cdTipoArquivo;
	}

	public void setCdTipoArquivo(int cdTipoArquivo) {
		this.cdTipoArquivo = cdTipoArquivo;
	}

	public GregorianCalendar getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public int getStArquivo() {
		return stArquivo;
	}

	public void setStArquivo(int stArquivo) {
		this.stArquivo = stArquivo;
	}

	public GregorianCalendar getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(GregorianCalendar dtInicial) {
		this.dtInicial = dtInicial;
	}

	public GregorianCalendar getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(GregorianCalendar dtFinal) {
		this.dtFinal = dtFinal;
	}

	public int getCdAssinatura() {
		return cdAssinatura;
	}

	public void setCdAssinatura(int cdAssinatura) {
		this.cdAssinatura = cdAssinatura;
	}

	public String getTxtOcr() {
		return txtOcr;
	}

	public void setTxtOcr(String txtOcr) {
		this.txtOcr = txtOcr;
	}

	public String getTxtCaminhoArquivo() {
		return txtCaminhoArquivo;
	}

	public void setTxtCaminhoArquivo(String txtCaminhoArquivo) {
		this.txtCaminhoArquivo = txtCaminhoArquivo;
	}

	public String getDsArquivo() {
		return dsArquivo;
	}

	public void setDsArquivo(String dsArquivo) {
		this.dsArquivo = dsArquivo;
	}

	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
	
	public Object clone() {
		return new ArquivoDTO(getnmTipoArquivo(),
				getCdArquivo(),
				getNmArquivo(),
				getDtArquivamento()==null ? null : (GregorianCalendar)getDtArquivamento().clone(),
				getNmDocumento(),
				getBlbArquivo(),
				getCdTipoArquivo(),
				getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
				getCdUsuario(),
				getStArquivo(),
				getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
				getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
				getCdAssinatura(),
				getTxtOcr(),
				getNrRegistros(),
				getTxtCaminhoArquivo(),
				getDsArquivo());
	}
}
