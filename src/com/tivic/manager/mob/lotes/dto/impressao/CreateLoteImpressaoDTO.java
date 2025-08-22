package com.tivic.manager.mob.lotes.dto.impressao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.lotes.dto.CreateAitLoteImpressaoDTO;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateLoteImpressaoDTO {
	private int cdCriador;
	private int tpImpressao;
	private List<CreateAitLoteImpressaoDTO> aits;
	
	public int getCdCriador() {
		return cdCriador;
	}
	
	public void setCdCriador(int cdCriador) {
		this.cdCriador = cdCriador;
	}
	
	public int getTpImpressao() {
		return tpImpressao;
	}

	public void setTpImpressao(int tpImpressao) {
		this.tpImpressao = tpImpressao;
	}
	
	public List<CreateAitLoteImpressaoDTO> getAits() {
		return aits;
	}
	
	public void setAits(List<CreateAitLoteImpressaoDTO> aits) {
		this.aits = aits;
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