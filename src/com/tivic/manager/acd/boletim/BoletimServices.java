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

public class BoletimServices {
	
	private Boletim boletim;

	public Boletim loadBoletim(int cdProfessor, int cdTurma, int cdMatricula){
        this.loadOfertas(cdProfessor, cdTurma);
        for(Oferta oferta : boletim.ofertas){
            this.loadDisciplinaAvaliacaoAluno(cdMatricula, oferta.getCdOferta());
            for(DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno : boletim.disciplinasAvaliacoesAlunos){
            	DisciplinaNota disciplinaNota 	= this.contemDisciplina(oferta.getCdDisciplina());
            	NotaUnidade notaUnidade 		= this.contemNotaUnidade(disciplinaNota, disciplinaAvaliacaoAluno);
                notaUnidade.setNota(notaUnidade.getNota() + disciplinaAvaliacaoAluno.getVlConceito());
                this.calcularTotal();
            }
        }
        return this.boletim;
    }
	
	private void loadOfertas(int cdProfessor, int cdTurma){
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cdProfessor", String.valueOf(cdProfessor), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_TURMA", String.valueOf(cdTurma), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmOfertas = OfertaServices.find(criterios);
		while(rsmOfertas.next()){
			boletim.ofertas.add(OfertaDAO.get(rsmOfertas.getInt("cd_oferta")));
		}
		
    }
	
	private void loadDisciplinaAvaliacaoAluno(int cdMatricula, int cdOferta){
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_MATRICULA", String.valueOf(cdMatricula), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_OFERTA", String.valueOf(cdOferta), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmDisciplinasAvaliacaoAluno = DisciplinaAvaliacaoAlunoServices.find(criterios);
		while(rsmDisciplinasAvaliacaoAluno.next()){
			boletim.disciplinasAvaliacoesAlunos.add(DisciplinaAvaliacaoAlunoDAO.get(rsmDisciplinasAvaliacaoAluno.getInt("cd_matricula"), rsmDisciplinasAvaliacaoAluno.getInt("cd_oferta_avaliacao"), rsmDisciplinasAvaliacaoAluno.getInt("cd_oferta")));
		}
    }

    private DisciplinaNota contemDisciplina(int cdDisciplina){
    	DisciplinaNota disciplinaNotaRetorno = null;
        for(DisciplinaNota disciplinaNota : boletim.disciplinasNotas){
            if(disciplinaNota.getDisciplina().getCdDisciplina() == cdDisciplina){
                disciplinaNotaRetorno = disciplinaNota;
            }
        }
        
        if(disciplinaNotaRetorno != null)
            return disciplinaNotaRetorno;

        disciplinaNotaRetorno = new DisciplinaNota(DisciplinaDAO.get(cdDisciplina));
        boletim.disciplinasNotas.add(disciplinaNotaRetorno);
        return disciplinaNotaRetorno;
    }
    
    private NotaUnidade contemNotaUnidade(DisciplinaNota disciplinaNota, DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno){
    	NotaUnidade notaUnidadeRetorno = null;
    	OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(disciplinaAvaliacaoAluno.getCdOfertaAvaliacao(), disciplinaAvaliacaoAluno.getCdOferta());
        for(NotaUnidade notaUnidade : disciplinaNota.getNotasUnidades()){
        	if(notaUnidade.getCursoUnidade().getCdUnidade() == ofertaAvaliacao.getCdUnidade()){
                notaUnidadeRetorno = notaUnidade;
            }
        }
        if(notaUnidadeRetorno != null)
            return notaUnidadeRetorno;

        return new NotaUnidade(CursoUnidadeDAO.get(ofertaAvaliacao.getCdUnidade(), ofertaAvaliacao.getCdCurso()));
    }


    private void calcularTotal(){
        for(DisciplinaNota disciplinaNota : boletim.disciplinasNotas){
            double total = 0;
            for(NotaUnidade notaUnidade : disciplinaNota.getNotasUnidades()){
            	total += notaUnidade.getNota();
            }
            if(disciplinaNota.getNotasUnidades().size() > 0)
                disciplinaNota.setTotal( total / disciplinaNota.getNotasUnidades().size());
        }
    }

}
