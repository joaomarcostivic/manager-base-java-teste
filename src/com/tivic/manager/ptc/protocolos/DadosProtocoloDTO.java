package com.tivic.manager.ptc.protocolos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.seg.Usuario;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosProtocoloDTO extends Documento{
	private AitMovimento movimento;
	private Ait ait;
	private Fase fase;
	private TipoDocumento tipoDocumento;
	private DocumentoPessoa relator;
	private Documento ata;
	private Agente agente;
	private Usuario usuario;
	private ApresentacaoCondutor apresentacaoCondutor;
	private List<Arquivo> arquivos;
	
	public DadosProtocoloDTO() {
		
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

	public DocumentoPessoa getRelator() {
		return relator;
	}

	public void setRelator(DocumentoPessoa relator) {
		this.relator = relator;
	}

	public Documento getAta() {
		return ata;
	}

	public void setAta(Documento ata) {
		this.ata = ata;
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

	public ApresentacaoCondutor getApresentacaoCondutor() {
		return apresentacaoCondutor;
	}

	public void setApresentacaoCondutor(ApresentacaoCondutor apresentacaoCondutor) {
		this.apresentacaoCondutor = apresentacaoCondutor;
	}

	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}
	
}
