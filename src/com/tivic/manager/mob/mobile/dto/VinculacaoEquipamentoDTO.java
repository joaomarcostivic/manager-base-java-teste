package com.tivic.manager.mob.mobile.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.seg.Usuario;

public class VinculacaoEquipamentoDTO {
	
	private Equipamento equipamento;
    private Usuario usuario;
  
    public VinculacaoEquipamentoDTO(Equipamento equipamento, Usuario usuario) {
        this.equipamento = equipamento;
        this.usuario = usuario;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
