package com.tivic.manager.mob.tabelashorarios;

import com.tivic.manager.mob.Horario;

public class HorarioDTO {
	
	private int cdHorario;
	private String hrTrecho;

	
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
	}
	
	public String getHrTrecho() {
		return hrTrecho;
	}
	public void setHrTrecho(String hrTrecho) {
		this.hrTrecho = hrTrecho;
	}
	
	public static class Builder {
		
		private HorarioDTO dto;
	
		
		public Builder(Horario dto) {
			try {
	           this.setCdHorario(this.dto.getCdHorario());
	           this.setHrTrecho(this.dto.getHrTrecho());
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		private void setHrTrecho(String hrTrecho) {
		    this.dto.setHrTrecho(hrTrecho);
		}

		private void setCdHorario(int cdHorario) {
		   this.dto.setCdHorario(cdHorario);
			
		}

		public HorarioDTO build() {
			return dto;
		}
		
	}

}
