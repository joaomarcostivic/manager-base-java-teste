package com.tivic.manager.util;

public class StatusProcesso {
	
	private int progresso;
	private int total;
	
	public StatusProcesso() {
		progresso = 0;
		total = 0;
	}
	public StatusProcesso(javax.servlet.http.HttpSession session, String name)	{
		progresso = 0;
		total = 0;
		session.setAttribute(name, this);
	}
	public int getProgresso() {
		return progresso;
	}
	
	public int getTotal() {
		return total;
	}
	
	public void setProgresso(int progresso) {
		this.progresso = progresso;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public void addProgresso() {
		this.progresso += 1;
	}
	
	public void addProgesso(int inc) {
		this.total += inc;
	}
}