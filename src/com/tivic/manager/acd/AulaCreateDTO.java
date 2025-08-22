package com.tivic.manager.acd;

import java.util.ArrayList;


public class AulaCreateDTO {
	
	Aula aula;
	ArrayList<AulaMatricula> aulasMatriculas;
	
	public AulaCreateDTO() {
		// TODO Auto-generated constructor stub
	}

	public AulaCreateDTO(Aula aula, ArrayList<AulaMatricula> aulasMatriculas) {
		super();
		this.aula = aula;
		this.aulasMatriculas = aulasMatriculas;
	}

	public Aula getAula() {
		return aula;
	}

	public void setAula(Aula aula) {
		this.aula = aula;
	}

	public ArrayList<AulaMatricula> getAulasMatriculas() {
		return aulasMatriculas;
	}

	public void setAulasMatriculas(ArrayList<AulaMatricula> aulasMatriculas) {
		this.aulasMatriculas = aulasMatriculas;
	}
	
}
