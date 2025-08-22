package com.tivic.manager.str.sync.logs.instance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Equipamento;
import com.tivic.manager.grl.EquipamentoDAO;
import com.tivic.manager.str.Ait;
import com.tivic.manager.str.AitImagem;
import com.tivic.manager.str.sync.logs.FlexSyncConfig;
import com.tivic.sol.connection.CustomConnection;

public class FlexAitLog implements FlexInstanceSync<Ait> {
	
	private List<Ait> data;
	private FlexSyncConfig config;
	
	public FlexAitLog() {};

	@Override
	public void write() throws Exception {
		String path = config.getLogPath() + "/aits";
		CustomConnection customConnection = new CustomConnection();
		String dfmt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		for(Ait ait : data) {
			Equipamento equipamento = EquipamentoDAO.get(ait.getCdEquipamento(), customConnection.getConnection());			
			
			File aitPath = new File(path + "/" + dfmt + "/" + equipamento.getNmEquipamento() + "/" + ait.getNrAit());
			
			if(!aitPath.exists()) aitPath.mkdirs();
			
			salvarArquivo(ait, path);
			salvarImagens(ait, path);
		}
		
	}

	public List<Ait> getData() {
		return data;
	}

	public void setData(List<Ait> aits) {
		this.data = aits;
	}

	public FlexSyncConfig getConfig() {
		return config;
	}

	public void setConfig(FlexSyncConfig config) {
		this.config = config;
	}
	
	private void salvarArquivo(Ait ait, String path) throws JsonProcessingException, IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + ait.getNrAit() + ".json"));
		try {
			ObjectMapper mapper = new ObjectMapper();				
			writer.write(mapper.writeValueAsString(ait));
		} finally {			
			writer.close();
		}		
	}
	
	private void salvarImagens(Ait ait, String path) throws IOException {		
		for(AitImagem imagem : ait.getImagens()) {
			FileOutputStream fos = new FileOutputStream(path + "/" + imagem.getCdImagem() + ".jpg");
			try {				
			    fos.write(imagem.getBlbImagem());
			} finally {
			    fos.close();
			}
		}		
	}
	
	

}
