package com.tivic.manager.mob.lote.impressao.remessacorreios;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.sol.report.ReportCriterios;

public class DadosDocumento {
	private int cdLoteNotificacao;
	private String nmDocumento;
	private ReportCriterios criteriosDocumentos;
	private List<DadosNotificacao> dadosNotificacaoList;
	private List<CorreiosEtiqueta> dadosEtiquetaList;
	private Arquivo arquivoPostagem;
	private Arquivo arquivoListaPostagem;
	
	public DadosDocumento() {
		this.criteriosDocumentos = new ReportCriterios();
		this.dadosEtiquetaList = new ArrayList<CorreiosEtiqueta>();
		this.dadosNotificacaoList = new ArrayList<DadosNotificacao>();
	}
	
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
	
	public ReportCriterios getCriteriosDocumentos() {
		return criteriosDocumentos;
	}
	
	public void setCriteriosDocumentos(ReportCriterios criteriosDocumentos) {
		this.criteriosDocumentos = criteriosDocumentos;
	}
	
	public List<DadosNotificacao> getDadosNotificacaoList() {
		return dadosNotificacaoList;
	}

	public void setDadosNotificacaoList(List<DadosNotificacao> dadosNotificacaoList) {
		this.dadosNotificacaoList = dadosNotificacaoList;
	}

	public List<CorreiosEtiqueta> getDadosEtiqueta() {
		return dadosEtiquetaList;
	}

	public void setDadosEtiqueta(List<CorreiosEtiqueta> dadosEtiquetaList) {
		this.dadosEtiquetaList = dadosEtiquetaList;
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
			JSONObject json = new JSONObject();
			json.put("cdLoteNotificacao", getCdLoteNotificacao());
			json.put("nmDocumento", getNmDocumento());
			json.put("criteriosDocumentos", getCriteriosDocumentos());
			json.put("dadosNotificacaoList", getDadosNotificacaoList().toString());
			json.put("dadosEtiqueta", getDadosEtiqueta().toString());
			json.put("arquivoPostagem", getArquivoPostagem().getNmArquivo());
			json.put("arquivoListaPostagem", getArquivoListaPostagem().getNmArquivo());
			return json.toString();
		} catch(Exception e) {
			return super.toString();
		}
	}
}
