package com.tivic.manager.acd.boletim;

import com.tivic.manager.acd.CursoUnidade;

public class NotaUnidade {

	private CursoUnidade cursoUnidade;
	private double nota;
	
	public NotaUnidade() {
		// TODO Auto-generated constructor stub
	}
	
	public NotaUnidade(CursoUnidade cursoUnidade) {
		super();
		this.cursoUnidade = cursoUnidade;
		this.nota = 0;
	}
	
	public NotaUnidade(CursoUnidade cursoUnidade, double nota) {
		super();
		this.cursoUnidade = cursoUnidade;
		this.nota = nota;
	}

	public CursoUnidade getCursoUnidade() {
		return cursoUnidade;
	}

	public void setCursoUnidade(CursoUnidade cursoUnidade) {
		this.cursoUnidade = cursoUnidade;
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) {
		this.nota = nota;
	}
	
	
	
}
