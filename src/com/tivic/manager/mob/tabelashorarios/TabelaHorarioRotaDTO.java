package com.tivic.manager.mob.tabelashorarios;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.TabelaHorario;


public class TabelaHorarioRotaDTO {
	

	private int cdLinha;
	private int cdRota;
	private int cdTabelaHorario;
	private String nmRota;
	private int tpRota;
	private List<ParadaDTO> paradas;
	private List<List<HorarioDTO>> horarios;
	private TabelaHorario tabelaHorario;
	
	
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	
	public int getCdLinha(){
		return this.cdLinha;
	}
	
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	
	public int getCdRota(){
		return this.cdRota;
	}
	
	public void setNmRota(String nmRota){
		this.nmRota=nmRota;
	}
	
	public String getNmRota(){
		return this.nmRota;
	}
	
	public void setTpRota(int tpRota){
		this.tpRota=tpRota;
	}
	public int getTpRota(){
		return this.tpRota;
	}
	
	public TabelaHorarioRotaDTO() {
		this.paradas  = new ArrayList<ParadaDTO>();
		this.horarios = new ArrayList <List<HorarioDTO>>();
	}
	
	public List<ParadaDTO> getParadas() {
		return paradas;
	}
	
	public void setParadas(List<ParadaDTO> paradas) {
		this.paradas = paradas;
	}
	
	public List<List<HorarioDTO>> getHorarios() {
		return horarios;
	}
	
	public void setHorarios(List<List<HorarioDTO>> list) {
		this.horarios = list;
	}
	
	public void addParadas(ParadaDTO paradas) {
		if(this.paradas == null)
			this.paradas = new ArrayList<ParadaDTO>();
		this.paradas.add(paradas);
	}
	

	public int getCdTabelaHorario() {
		return cdTabelaHorario;
	}

	public void setCdTabelaHorario(int cdTabelaHorario) {
		this.cdTabelaHorario = cdTabelaHorario;
	}

	public TabelaHorario getTabelaHorario() {
		return tabelaHorario;
	}

	public void setTabelaHorario(TabelaHorario tabelaHorario) {
		this.tabelaHorario = tabelaHorario;
	}
}
