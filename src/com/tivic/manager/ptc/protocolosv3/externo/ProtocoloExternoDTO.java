package com.tivic.manager.ptc.protocolosv3.externo;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.grl.Arquivo;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ProtocoloExternoDTO {

	private int cdDocumentoExterno;
	private int cdDocumento;
	private int cdApresentacaoCondutor;
	private int cdInfracao;
	private int cdUsuario;
	private int cdTipoDocumento;
	private int tpModeloCnh; 
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEntrada;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	private String idAit;
	private String nrPlaca;
	private String nrDocumento;
	private String nmRequerente;
	private String txtObservacao;
	private String nrRenainf;
	private String nmCondutor;
	private String nrRg;
	private String nrCpfCnpj;
	private String nrCnh;
	private String ufCnh;
	private int tpCategoriaCnh;
	private String idPaisCnh;
	private String nrTelefone1;
	private String dsEndereco;
	private String dsComplementoEndereco;
	private String nmCidade;
	private String nrEndereco;
	private int cdOrgaoExterno;
	private String sgOrgaoExterno;
	private String nmOrgaoExterno;
	private List<Arquivo> arquivos;
	
	public int getCdDocumentoExterno() {
		return cdDocumentoExterno;
	}
	
	public void setCdDocumentoExterno(int cdDocumentoExterno) {
		this.cdDocumentoExterno = cdDocumentoExterno;
	}
	
	public int getCdDocumento() {
		return cdDocumento;
	}
	
	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}
	
	public int getCdApresentacaoCondutor() {
		return cdApresentacaoCondutor;
	}

	public void setCdApresentacaoCondutor(int cdApresentacaoCondutor) {
		this.cdApresentacaoCondutor = cdApresentacaoCondutor;
	}

	public int getCdInfracao() {
		return cdInfracao;
	}
	
	public void setCdInfracao(int cdInfracao) {
		this.cdInfracao = cdInfracao;
	}
	
	public String getIdAit() {
		return idAit;
	}
	
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	
	public String getNrPlaca() {
		return nrPlaca;
	}
	
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	
	public GregorianCalendar getDtEntrada() {
		return dtEntrada;
	}
	
	public void setDtEntrada(GregorianCalendar dtEntrada) {
		this.dtEntrada = dtEntrada;
	}
	
	public GregorianCalendar getDtProtocolo() {
		return dtProtocolo;
	}
	
	public void setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}
	
	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}
	
	public String getNmRequerente() {
		return nmRequerente;
	}
	
	public void setNmRequerente(String nmRequerente) {
		this.nmRequerente = nmRequerente;
	}
	
	public String getTxtObservacao() {
		return txtObservacao;
	}
	
	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	
	public String getNrRenainf() {
		return nrRenainf;
	}

	public void setNrRenainf(String nrRenainf) {
		this.nrRenainf = nrRenainf;
	}
	
	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}

	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}

	public int getTpModeloCnh() {
		return tpModeloCnh;
	}

	public void setTpModeloCnh(int tpModeloCnh) {
		this.tpModeloCnh = tpModeloCnh;
	}

	public String getNmCondutor() {
		return nmCondutor;
	}

	public void setNmCondutor(String nmCondutor) {
		this.nmCondutor = nmCondutor;
	}

	public String getNrRg() {
		return nrRg;
	}

	public void setNrRg(String nrRg) {
		this.nrRg = nrRg;
	}

	public String getNrCpfCnpj() {
		return nrCpfCnpj;
	}

	public void setNrCpfCnpj(String nrCpfCnpj) {
		this.nrCpfCnpj = nrCpfCnpj;
	}

	public String getNrCnh() {
		return nrCnh;
	}

	public void setNrCnh(String nrCnh) {
		this.nrCnh = nrCnh;
	}

	public String getUfCnh() {
		return ufCnh;
	}

	public void setUfCnh(String ufCnh) {
		this.ufCnh = ufCnh;
	}

	public int getTpCategoriaCnh() {
		return tpCategoriaCnh;
	}

	public void setTpCategoriaCnh(int tpCategoriaCnh) {
		this.tpCategoriaCnh = tpCategoriaCnh;
	}

	public String getIdPaisCnh() {
		return idPaisCnh;
	}

	public void setIdPaisCnh(String idPaisCnh) {
		this.idPaisCnh = idPaisCnh;
	}

	public String getNrTelefone1() {
		return nrTelefone1;
	}

	public void setNrTelefone1(String nrTelefone1) {
		this.nrTelefone1 = nrTelefone1;
	}

	public String getDsEndereco() {
		return dsEndereco;
	}

	public void setDsEndereco(String dsEndereco) {
		this.dsEndereco = dsEndereco;
	}

	public String getDsComplementoEndereco() {
		return dsComplementoEndereco;
	}

	public void setDsComplementoEndereco(String dsComplementoEndereco) {
		this.dsComplementoEndereco = dsComplementoEndereco;
	}

	public String getNmCidade() {
		return nmCidade;
	}

	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}
	
	public String getNrEndereco() {
		return nrEndereco;
	}

	public void setNrEndereco(String nrEndereco) {
		this.nrEndereco = nrEndereco;
	}

	public int getCdOrgaoExterno() {
		return cdOrgaoExterno;
	}

	public void setCdOrgaoExterno(int cdOrgaoExterno) {
		this.cdOrgaoExterno = cdOrgaoExterno;
	}
	
	public String getSgOrgaoExterno() {
		return sgOrgaoExterno;
	}

	public void setSgOrgaoExterno(String sgOrgaoExterno) {
		this.sgOrgaoExterno = sgOrgaoExterno;
	}
	
	public String getNmOrgaoExterno() {
		return nmOrgaoExterno;
	}

	public void setNmOrgaoExterno(String nmOrgaoExterno) {
		this.nmOrgaoExterno = nmOrgaoExterno;
	}
	
	public List<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
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