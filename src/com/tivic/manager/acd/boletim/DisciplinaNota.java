package com.tivic.manager.acd.boletim;

import java.util.ArrayList;

import com.tivic.manager.acd.Disciplina;

public class DisciplinaNota {

	private Disciplina disciplina;
	private ArrayList<NotaUnidade> notasUnidades;
	private double total;
	
	public DisciplinaNota() {
		// TODO Auto-generated constructor stub
	}

	public DisciplinaNota(Disciplina disciplina) {
		super();
		this.disciplina = disciplina;
		this.notasUnidades = new ArrayList<NotaUnidade>();
		this.total = 0;
	}
	
	public DisciplinaNota(Disciplina disciplina, ArrayList<NotaUnidade> notasUnidades, double total) {
		super();
		this.disciplina = disciplina;
		this.notasUnidades = notasUnidades;
		this.total = total;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public ArrayList<NotaUnidade> getNotasUnidades() {
		return notasUnidades;
	}

	public void setNotasUnidades(ArrayList<NotaUnidade> notasUnidades) {
		this.notasUnidades = notasUnidades;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	
	
}
