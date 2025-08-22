package com.tivic.manager.mob;

import java.util.List;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.seg.Usuario;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AitMovimentoDocumentoDTO {
	
	private Documento documento;
	private Fase fase;
	private TipoDocumento tipoDocumento;
	private List<FormularioAtributoValor> camposFormulario;
	private List<DocumentoArquivo> arquivosAnexados;
	private Ait ait;
	private Documento documentoSuperior;	
	private AitMovimento movimento;
	private Agente agente;
	private Usuario usuario;
	private DocumentoPessoa documentoPessoa;
	private DocumentoOcorrencia documentoOcorrencia;
	private ApresentacaoCondutor apresentacaoCondutor;
	private SituacaoDocumento situacaoDocumento;
	private List<Arquivo> arquivos;
	private String nmUsuarioResponsavel;

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public List<FormularioAtributoValor> getCamposFormulario() {
		return camposFormulario;
	}

	public void setCamposFormulario(List<FormularioAtributoValor> camposFormulario) {
		this.camposFormulario = camposFormulario;
	}


	public AitMovimento getMovimento() {
		return movimento;
	}

	public void setMovimento(AitMovimento movimento) {
		this.movimento = movimento;
	}

	public Ait getAit() {
		return ait;
	}

	public void setAit(Ait ait) {
		this.ait = ait;
	}

	public Fase getFase() {
		return fase;
	}

	public void setFase(Fase fase) {
		this.fase = fase;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public Documento getDocumentoSuperior() {
		return documentoSuperior;
	}
	
	public void setDocumentoSuperior(Documento documentoSuperior) {
		this.documentoSuperior = documentoSuperior;
	}
	
	public Agente getAgente() {
		return agente;
	}
	
	public void setAgente(Agente agente) {
		this.agente = agente;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public DocumentoPessoa getDocumentoPessoa() {
		return this.documentoPessoa;
	}
	
	public void setDocumentoPessoa(DocumentoPessoa documentoPessoa) {
		this.documentoPessoa = documentoPessoa;
	}
	
	public String getNmUsuarioResponsavel() {
		return this.nmUsuarioResponsavel;
	}

	public void setNmUsuarioResponsavel(String nmUsuarioResponsavel) {
		this.nmUsuarioResponsavel = nmUsuarioResponsavel;
	}
	
	@Override
	public String toString() {
		try {
			JSONObject json = new JSONObject();
			json.put("documento", getDocumento());
			json.put("fase", getFase());
			json.put("tipoDocumento", getTipoDocumento());
			json.put("camposFormulario", getCamposFormulario());
			json.put("ait", getAit());
			json.put("documentoSuperior", getDocumentoSuperior());
			json.put("movimento", getMovimento());
			json.put("agente", getAgente());
			json.put("usuario", getUsuario());
			json.put("documentoPessoa", getDocumentoPessoa());
			json.put("documentoOcorrencia", getDocumentoOcorrencia());
			json.put("apresentacaoCondutor", getApresentacaoCondutor());
			json.put("situacaoDocumento", getSituacaoDocumento());
			json.put("arquivos", getArquivos());
			return json.toString();
		} catch(Exception e) {
			return super.toString();
		}
		
	}

	public DocumentoOcorrencia getDocumentoOcorrencia() {
		return documentoOcorrencia;
	}

	public void setDocumentoOcorrencia(DocumentoOcorrencia documentoOcorrencia) {
		this.documentoOcorrencia = documentoOcorrencia;
	}

	public ApresentacaoCondutor getApresentacaoCondutor() {
		return apresentacaoCondutor;
	}

	public void setApresentacaoCondutor(ApresentacaoCondutor apresentacaoCondutor) {
		this.apresentacaoCondutor = apresentacaoCondutor;
	}

	public SituacaoDocumento getSituacaoDocumento() {
		return situacaoDocumento;
	}

	public void setSituacaoDocumento(SituacaoDocumento situacaoDocumento) {
		this.situacaoDocumento = situacaoDocumento;
	}

	public List<DocumentoArquivo> getArquivosAnexados() {
		return arquivosAnexados;
	}

	public void setArquivosAnexados(List<DocumentoArquivo> arquivosAnexados) {
		this.arquivosAnexados = arquivosAnexados;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

}
