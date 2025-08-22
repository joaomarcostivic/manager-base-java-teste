package com.tivic.manager.ptc.generators;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.gpn.tipodocumento.TipoDocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraNumeroEdital implements IGeraNumeroEdital{
	
	TipoDocumentoRepository tipoDocumentoRepository;
	
	public GeraNumeroEdital() throws Exception {
		this.tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
	}
	
	@Override  
	public String gerar(int cdTipoDocumento) throws Exception {
		TipoDocumento tipoDocumento = this.tipoDocumentoRepository.get(cdTipoDocumento);
		if(tipoDocumento != null) {
			String ultimoNumeroAno = String.valueOf(tipoDocumento.getNrUltimaNumeracao());
			int ano = getAno();
			int ultimoNumero = 0;
			if(ultimoNumeroAno.length() > 1) {
				int ultimoAno = Integer.parseInt(ultimoNumeroAno.substring(ultimoNumeroAno.length() - 4));
				ultimoNumero = Integer.parseInt(ultimoNumeroAno.substring(0, ultimoNumeroAno.length() - 4));
				if(ano > ultimoAno) {
					 ultimoNumero = 0;
				}
			}
			String nrEdital = String.format("%03d", ultimoNumero + 1)  + "/" + String.valueOf(ano);
	        updateUltimaNumeracao(tipoDocumento, nrEdital, new CustomConnection());
	        return nrEdital != null ? nrEdital : null;
		}
		return null;
	}
	
	private int getAno() {
		Date data = new Date();
        LocalDate localDate = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int ano = localDate.getYear();
        return ano;
	}
	
	private void updateUltimaNumeracao(TipoDocumento tipoDocumento, String nrEdital, CustomConnection customConnection) throws Exception {
		try {
			String[] partes = nrEdital.split("/");
			int ultimaNumeracao = Integer.parseInt(partes[0] + partes[1]);
			tipoDocumento.setNrUltimaNumeracao(ultimaNumeracao);
			customConnection.initConnection(true);
			this.tipoDocumentoRepository.update(tipoDocumento, customConnection);
			customConnection.finishConnection();
		} finally { 
			customConnection.closeConnection();
		}
	} 

}
