package com.tivic.manager.mob.tabelashorarios;

import com.tivic.manager.mob.LinhaTrecho;

public class ParadaDTO {
	
	private int cdTrecho;
	private String nmGrupoParadaSuperior;
	private int cdTrechoAnterior;
	
	public void setCdTrecho(int cdTrecho){
		this.cdTrecho=cdTrecho;
	}
	public int getCdTrecho(){
		return this.cdTrecho;
	}
	
	public void setNmGrupoParadaSuperior(String nmGrupoParadaSuperior){
		this.nmGrupoParadaSuperior=nmGrupoParadaSuperior;
	}
	public String getNmGrupoParadaSuperior(){
		return this.nmGrupoParadaSuperior;
	}

	public int getCdTrechoAnterior() {
		return cdTrechoAnterior;
	}
	public void setCdTrechoAnterior(int cdTrechoAnterior) {
		this.cdTrechoAnterior = cdTrechoAnterior;
	}


	public static class Builder {
		
		private ParadaDTO dto;

		
		public Builder(ParadaDTO dto) {
			try {
				this.setCdTrecho(this.dto.getCdTrecho());
				this.setNmGrupoParadaSuperior(this.dto.getNmGrupoParadaSuperior());
				this.setCdTrechoAnterior(this.dto.getCdTrechoAnterior());
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		
		private void setNmGrupoParadaSuperior(String nmGrupoParadaSuperior) {
	      this.dto.setNmGrupoParadaSuperior(nmGrupoParadaSuperior);	
		}

		private void setCdTrecho(int cdTrecho) {
		  this.dto.setCdTrecho(cdTrecho);	
		}
		
		private void setCdTrechoAnterior(int cdTrechoAnterior) {
			 this.dto.setCdTrechoAnterior(cdTrechoAnterior);	
		}

		public ParadaDTO build() {
			return dto;
		}
		
	}
    
}
