package com.tivic.manager.triagem.dtos;

public class ImagemNotificacaoDTO {

    private Double vlLatitude;
    private Double vlLongitude;
    private String url;
    private byte[] blbImagem;
    
	public Double getVlLatitude() {
		return vlLatitude;
	}
	
	public void setVlLatitude(Double vlLatitude) {
		this.vlLatitude = vlLatitude;
	}
	
	public Double getVlLongitude() {
		return vlLongitude;
	}
	
	public void setVlLongitude(Double vlLongitude) {
		this.vlLongitude = vlLongitude;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public byte[] getBlbImagem() {
		return blbImagem;
	}

	public void setBlbImagem(byte[] blbImagem) {
		this.blbImagem = blbImagem;
	}
    
}
