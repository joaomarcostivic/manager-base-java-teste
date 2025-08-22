package com.tivic.manager.acd.boletim;

import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.acd.CursoUnidadeDAO;
import com.tivic.manager.acd.DisciplinaAvaliacaoAluno;
import com.tivic.manager.acd.DisciplinaAvaliacaoAlunoDAO;
import com.tivic.manager.acd.DisciplinaAvaliacaoAlunoServices;
import com.tivic.manager.acd.DisciplinaDAO;
import com.tivic.manager.acd.Oferta;
import com.tivic.manager.acd.OfertaAvaliacao;
import com.tivic.manager.acd.OfertaAvaliacaoDAO;
import com.tivic.manager.acd.OfertaDAO;
import com.tivic.manager.acd.OfertaServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class Boletim {

	public ArrayList<DisciplinaNota> disciplinasNotas;
	public ArrayList<Oferta> ofertas;
	public ArrayList<DisciplinaAvaliacaoAluno> disciplinasAvaliacoesAlunos;
	
}
