package com.tivic.manager.mob.ait.imagemvideo.aitvideo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AitVideoSearch {
	
	private int cdAit;
	private int cdImagem;
	
	public int getCdAit() {
		return cdAit;
	}
	
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	
	public int getCdImagem() {
		return cdImagem;
	}
	
	public void setCdImagem(int cdImagem) {
		this.cdImagem = cdImagem;
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
