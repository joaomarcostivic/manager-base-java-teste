package com.tivic.manager.mob.mobile.builders;

import java.util.UUID;

import com.tivic.manager.mob.mobile.dto.GeradorQrCodeDTO;

public class ParametrosQrCodeBuilder {
	
	private GeradorQrCodeDTO geradorQrCodeDTO;
	
	public ParametrosQrCodeBuilder() {
		geradorQrCodeDTO = new GeradorQrCodeDTO();
	}
	
	public ParametrosQrCodeBuilder addLgApiSsl(String lgApiSsl) {
		 geradorQrCodeDTO.setLgApiSsl(lgApiSsl.equals("1") ? true : false);
		 return this;
	}
		
	public ParametrosQrCodeBuilder addNmApiHost(String nmApiHost) {
		geradorQrCodeDTO.setNmApiHost(nmApiHost);
	    return this;
	}
	
	public ParametrosQrCodeBuilder addNmApiPort(String nmApiPort) {
		geradorQrCodeDTO.setNmApiPort(nmApiPort);
	    return this;
	}
	
	public ParametrosQrCodeBuilder addNmApiContext(String nmApiContext) {
		geradorQrCodeDTO.setNmApiContext(nmApiContext);
	    return this;
	}
	
	public ParametrosQrCodeBuilder addNmApiRoot(String nmApiRoot) {
		geradorQrCodeDTO.setNmApiRoot(nmApiRoot);
	    return this;
	}
	
	public ParametrosQrCodeBuilder addDsDeviceUuid(String dsDeviceUuid) {
		if (dsDeviceUuid == null || dsDeviceUuid.isEmpty()) {
			String idDivice = UUID.randomUUID().toString();
			geradorQrCodeDTO.setDsDeviceUuid(idDivice.toUpperCase());
			return this;
	    }
		geradorQrCodeDTO.setDsDeviceUuid(dsDeviceUuid);
		return this;
	}
	
	public ParametrosQrCodeBuilder addNmEquipamento(String nmEquipamento) {
		geradorQrCodeDTO.setNmEquipamento(nmEquipamento);
	    return this;
	}
	
	public ParametrosQrCodeBuilder addCdEquipamento(int cdEquipamento) {
		geradorQrCodeDTO.setCdEquipamento(cdEquipamento);
	    return this;
	}

	public GeradorQrCodeDTO build() {
		return geradorQrCodeDTO;
	}

}
