package com.tivic.manager.ptc.protocolosv3;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.seg.Usuario;

public class ProtocoloDTO {

	private Ait ait;
	private AitMovimento aitMovimento;
	private Documento documento;
	private Fase fase;
	private TipoDocumento tipoDocumento;
	private Usuario usuario;
	private SituacaoDocumento situacaoDocumento;
	private List<Arquivo> arquivos;
	private String emailSolicitante;
	private String cpfSolicitante;
	
	public ProtocoloDTO() {
		this.ait = new Ait();
		this.aitMovimento = new AitMovimento();
		this.documento = new Documento();
		this.fase = new Fase();
		this.tipoDocumento = new TipoDocumento();
		this.usuario = new Usuario();
		this.situacaoDocumento = new SituacaoDocumento();
		this.arquivos = new ArrayList<Arquivo>();
	}
	
	public Ait getAit() {
		return ait;
	}
	public void setAit(Ait ait) {
		this.ait = ait;
	}
	public AitMovimento getAitMovimento() {
		return aitMovimento;
	}
	public void setAitMovimento(AitMovimento aitMovimento) {
		this.aitMovimento = aitMovimento;
	}
	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	public List<Arquivo> getArquivos() {
		return arquivos;
	}
	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
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
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public SituacaoDocumento getSituacaoDocumento() {
		return situacaoDocumento;
	}
	public void setSituacaoDocumento(SituacaoDocumento situacaoDocumento) {
		this.situacaoDocumento = situacaoDocumento;
	}
	
	public String getEmailSolicitante() {
		return emailSolicitante;
	}

	public void setEmailSolicitante(String emailSolicitante) {
		this.emailSolicitante = emailSolicitante;
	}

	public String getCpfSolicitante() {
		return cpfSolicitante;
	}

	public void setCpfSolicitante(String cpfSolicitante) {
		this.cpfSolicitante = cpfSolicitante;
	}

	@Override
	 public String toString() {
       try {
           return new ObjectMapper().writeValueAsString(this);
       } catch (JsonProcessingException e) {
           return "Não foi possível serializar o objeto informado";
       }
   }
}
