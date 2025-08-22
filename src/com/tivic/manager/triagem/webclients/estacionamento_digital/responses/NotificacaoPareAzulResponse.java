package com.tivic.manager.triagem.webclients.estacionamento_digital.responses;

import java.util.GregorianCalendar;
import java.util.List;

public class NotificacaoPareAzulResponse {

    private int notificacaoId;
    private String codigo;
    private String veiculoPlaca;
    private GregorianCalendar dataCriacao;
    private Long latitude;
    private Long longitude;
    private String enderecoLogradouro;
    private String enderecoNumero;
    private String enderecoBairro;
    private String motivoNotificacao;
    private String veiculoMarcaModelo;
    private List<Imagens> imagens;
    
	public int getNotificacaoId() {
		return notificacaoId;
	}
	public void setNotificacaoId(int notificacaoId) {
		this.notificacaoId = notificacaoId;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getVeiculoPlaca() {
		return veiculoPlaca;
	}
	public void setVeiculoPlaca(String veiculoPlaca) {
		this.veiculoPlaca = veiculoPlaca;
	}
	public GregorianCalendar getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(GregorianCalendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Long getLatitude() {
		return latitude;
	}
	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}
	public Long getLongitude() {
		return longitude;
	}
	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}
	public String getEnderecoLogradouro() {
		return enderecoLogradouro;
	}
	public void setEnderecoLogradouro(String enderecoLogradouro) {
		this.enderecoLogradouro = enderecoLogradouro;
	}
	public String getEnderecoNumero() {
		return enderecoNumero;
	}
	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}
	public String getEnderecoBairro() {
		return enderecoBairro;
	}
	public void setEnderecoBairro(String enderecoBairro) {
		this.enderecoBairro = enderecoBairro;
	}
	public String getMotivoNotificacao() {
		return motivoNotificacao;
	}
	public void setMotivoNotificacao(String motivoNotificacao) {
		this.motivoNotificacao = motivoNotificacao;
	}
	public String getVeiculoMarcaModelo() {
		return veiculoMarcaModelo;
	}
	public void setVeiculoMarcaModelo(String veiculoMarcaModelo) {
		this.veiculoMarcaModelo = veiculoMarcaModelo;
	}
	public List<Imagens> getImagens() {
		return imagens;
	}
	public void setImagens(List<Imagens> imagens) {
		this.imagens = imagens;
	}
    
    
	
}
