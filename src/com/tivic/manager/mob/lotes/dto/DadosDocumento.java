package com.tivic.manager.mob.lotes.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.sol.report.ReportCriterios;

public class DadosDocumento { 
	private int cdLoteNotificacao;
	private String nmDocumento;
	private ReportCriterios criterios = new ReportCriterios();
	private List<Notificacao> notificacoes = new ArrayList<Notificacao>();
	private List<CorreiosEtiqueta> etiquetas = new ArrayList<CorreiosEtiqueta>();
	private Arquivo arquivoPostagem;
	private Arquivo arquivoListaPostagem;
	
	public int getCdLoteNotificacao() {
		return cdLoteNotificacao;
	}

	public void setCdLoteNotificacao(int cdLoteNotificacao) {
		this.cdLoteNotificacao = cdLoteNotificacao;
	}

	public String getNmDocumento() {
		return nmDocumento;
	}
	
	public void setNmDocumento(String nmDocumento) {
		this.nmDocumento = nmDocumento;
	}
	
	public ReportCriterios getCriterios() {
		return criterios;
	}
	
	public void setCriterios(ReportCriterios criteriosDocumentos) {
		this.criterios = criteriosDocumentos;
	}
	
	public List<Notificacao> getNotificacoes() {
		return notificacoes;
	}

	public void setNotificacoes(List<Notificacao> notificacoes) {
		this.notificacoes = notificacoes;
	}

	public List<CorreiosEtiqueta> getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(List<CorreiosEtiqueta> dadosEtiquetaList) {
		this.etiquetas = dadosEtiquetaList;
	}
	
	public Arquivo getArquivoPostagem() {
		return arquivoPostagem;
	}

	public void setArquivoPostagem(Arquivo arquivoPostagem) {
		this.arquivoPostagem = arquivoPostagem;
	}

	public Arquivo getArquivoListaPostagem() {
		return arquivoListaPostagem;
	}

	public void setArquivoListaPostagem(Arquivo arquivoListaPostagem) {
		this.arquivoListaPostagem = arquivoListaPostagem;
	}
	
	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
	
}
