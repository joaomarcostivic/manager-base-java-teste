package com.tivic.manager.crm;

import java.util.ArrayList;

public class SessaoAtendimento {

	private Atendente atendente;
	private ArrayList<SalaAtendimento> atendimentos;

	public SessaoAtendimento(Atendente atendente){
		setAtendente(atendente);
		atendimentos = new ArrayList<SalaAtendimento>();
	}
	public void setAtendente(Atendente atendente){
		this.atendente=atendente;
	}
	public Atendente getAtendente(){
		return this.atendente;
	}
	
	public void addAtendimento(SalaAtendimento atendimento){
		atendimentos.add(atendimento);
	}
	
	public SalaAtendimento getAtendimento(int index){
		return atendimentos.get(index);
	}
	
	public int getQtAtendimentos(){
		return atendimentos.size();
	}
	
	public int removeAtendimento(int index){
		if(atendimentos.remove(index)!=null)
			return 1;
		else
			return 0;
	}
}
