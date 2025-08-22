package com.tivic.manager.mob.mobile.dto;

import java.awt.image.BufferedImage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeradorQrCodeDTO {

    private BufferedImage qrCodeImage;
    private Boolean lgApiSsl;
    private String nmApiHost;
    private String nmApiPort;
    private String nmApiContext;
    private String nmApiRoot;
    private String dsDeviceUuid;
    private String nmEquipamento;
    private int cdEquipamento;

    public BufferedImage getQrCodeImage() {
		return qrCodeImage;
	}

	public void setQrCodeImage(BufferedImage qrCodeImage) {
		this.qrCodeImage = qrCodeImage;
	}

	public Boolean getLgApiSsl() {
		return lgApiSsl;
	}

	public void setLgApiSsl(Boolean lgApiSsl) {
		this.lgApiSsl = lgApiSsl;
	}

	public String getNmApiHost() {
		return nmApiHost;
	}

	public void setNmApiHost(String nmApiHost) {
		this.nmApiHost = nmApiHost;
	}

	public String getNmApiPort() {
		return nmApiPort;
	}

	public void setNmApiPort(String nmApiPort) {
		this.nmApiPort = nmApiPort;
	}

	public String getNmApiContext() {
		return nmApiContext;
	}

	public void setNmApiContext(String nmApiContext) {
		this.nmApiContext = nmApiContext;
	}

	public String getNmApiRoot() {
		return nmApiRoot;
	}

	public void setNmApiRoot(String nmApiRoot) {
		this.nmApiRoot = nmApiRoot;
	}

	public String getDsDeviceUuid() {
		return dsDeviceUuid;
	}

	public void setDsDeviceUuid(String dsDeviceUuid) {
		this.dsDeviceUuid = dsDeviceUuid;
	}

	public String getNmEquipamento() {
		return nmEquipamento;
	}

	public void setNmEquipamento(String nmEquipamento) {
		this.nmEquipamento = nmEquipamento;
	}

	public int getCdEquipamento() {
		return cdEquipamento;
	}

	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
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
