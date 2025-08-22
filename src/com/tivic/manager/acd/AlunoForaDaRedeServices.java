package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.TipoTransporteServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class AlunoForaDaRedeServices {

	public static final int TP_SITUACAO_VINCULADO_POR_OUTRA_ESCOLA 				= 0;
	public static final int TP_SITUACAO_CONFIRMADO_ALUNO_FORA_DA_REDE 			= 1;
	public static final int TP_SITUACAO_CONTATO_FEITO_ESPERANDO_DESVINCULACAO 	= 2;
	public static final int TP_SITUACAO_NECESSIDADE_ENVIAR_FREQUENCIA		 	= 3;
	public static final int TP_SITUACAO_DESVINCULACAO_FEITA		 				= 4;
	
	public static final String[] tiposSituacao = {"Vinculado por outra escola", "Confirmado que o aluno estava fora da rede", "Contato feito - Esperando desvinculação", "Necessidade de enviar frequência", "Desvinculação realizada"};
	
	public static Result save(AlunoForaDaRede alunoForaDaRede){
		return save(alunoForaDaRede, null, null);
	}
	
	public static Result save(AlunoForaDaRede alunoForaDaRede, Connection connect){
		return save(alunoForaDaRede, null, connect);
	}

	public static Result save(AlunoForaDaRede alunoForaDaRede, AuthData authData){
		return save(alunoForaDaRede, authData, null);
	}

	public static Result save(AlunoForaDaRede alunoForaDaRede, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(alunoForaDaRede==null)
				return new Result(-1, "Erro ao salvar. AlunoForaDaRede é nulo");

			int retorno;
			if(AlunoForaDaRedeDAO.get(alunoForaDaRede.getCdAluno(), alunoForaDaRede.getCdInstituicao(), alunoForaDaRede.getCdTurma(), alunoForaDaRede.getCdMatricula(), connect)==null){
				retorno = AlunoForaDaRedeDAO.insert(alunoForaDaRede, connect);
				alunoForaDaRede.setCdAluno(retorno);
			}
			else {
				retorno = AlunoForaDaRedeDAO.update(alunoForaDaRede, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ALUNOFORADAREDE", alunoForaDaRede);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveSituacao(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula, int tpSituacao){
		return saveSituacao(cdAluno, cdInstituicao, cdTurma, cdMatricula, tpSituacao, null);
	}
	
	public static Result saveSituacao(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula, int tpSituacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			AlunoForaDaRede alunoForaDaRede = AlunoForaDaRedeDAO.get(cdAluno, cdInstituicao, cdTurma, cdMatricula, connect);
			
			if(alunoForaDaRede==null)
				return new Result(-1, "Erro ao salvar. AlunoForaDaRede é nulo");

			alunoForaDaRede.setTpSituacao(tpSituacao);
			Result result = save(alunoForaDaRede, connect);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Salvo com sucesso...", "ALUNOFORADAREDE", alunoForaDaRede);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result adicionar(int cdAluno, int cdMatricula){
		return adicionar(cdAluno, cdMatricula, null);
	}

	public static Result adicionar(int cdAluno, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
			Turma turma = TurmaDAO.get(matricula.getCdTurma(), connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
			
			AlunoForaDaRede alunoForaDaRede = new AlunoForaDaRede();
			alunoForaDaRede.setCdAluno(cdAluno);
			alunoForaDaRede.setCdMatricula(cdMatricula);
			alunoForaDaRede.setCdTurma(turma.getCdTurma());
			alunoForaDaRede.setCdInstituicao(instituicao.getCdInstituicao());
			alunoForaDaRede.setDtRegistro(Util.getDataAtual());
			alunoForaDaRede.setTpSituacao(TP_SITUACAO_VINCULADO_POR_OUTRA_ESCOLA);
			
			Result result = save(alunoForaDaRede, connect);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			
			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Salvo com sucesso...", "ALUNOFORADAREDE", alunoForaDaRede);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(AlunoForaDaRede alunoForaDaRede) {
		return remove(alunoForaDaRede.getCdAluno(), alunoForaDaRede.getCdInstituicao(), alunoForaDaRede.getCdTurma(), alunoForaDaRede.getCdMatricula());
	}
	public static Result remove(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula){
		return remove(cdAluno, cdInstituicao, cdTurma, cdMatricula, false, null, null);
	}
	public static Result remove(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula, boolean cascade){
		return remove(cdAluno, cdInstituicao, cdTurma, cdMatricula, cascade, null, null);
	}
	public static Result remove(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula, boolean cascade, AuthData authData){
		return remove(cdAluno, cdInstituicao, cdTurma, cdMatricula, cascade, authData, null);
	}
	public static Result remove(int cdAluno, int cdInstituicao, int cdTurma, int cdMatricula, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = AlunoForaDaRedeDAO.delete(cdAluno, cdInstituicao, cdTurma, cdMatricula, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aluno_fora_da_rede");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findAtual() {
		return findAtual(null);
	}

	public static ResultSetMap findAtual(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoSecretaria = 0;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connect);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("J.cd_periodo_letivo_superior", "" + cdPeriodoSecretaria, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			while(rsm.next()){
				
				ResultSetMap rsmSituacao = new ResultSetMap();
				for(int i = 0; i < tiposSituacao.length; i++){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("TP_SITUACAO", i);
					register.put("NM_TP_SITUACAO", tiposSituacao[i]);
					rsmSituacao.addRegister(register);
				}
				rsmSituacao.beforeFirst();
				
				rsm.setValueToField("TIPOSSITUACAO", rsmSituacao);
				
				rsm.setValueToField("NM_TP_SITUACAO", tiposSituacao[rsm.getInt("TP_SITUACAO")]);
				
				Turma turma = TurmaDAO.get(rsm.getInt("cd_turma"), connect);
				rsm.setValueToField("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
				
				InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"), connect);
				rsm.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[instituicaoEducacenso.getTpLocalizacao()]);
				
				GregorianCalendar dtNascimento = rsm.getGregorianCalendar("dt_nascimento");
				if(dtNascimento != null)
					rsm.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR));
			}
			
			rsm.beforeFirst();

			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AlunoForaDaRedeServices.findAtual: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, H.nm_pessoa AS NM_INSTITUICAO, F.nm_produto_servico AS NM_CURSO, D.nm_turma, (F.nm_produto_servico || ' - ' || D.nm_turma) AS NM_TURMA_COMPLETA, C.nm_pessoa AS NM_ALUNO, I.nr_matricula, J.cd_periodo_letivo, D.cd_instituicao, D.cd_periodo_letivo, CC.dt_nascimento, CC.nm_mae FROM acd_aluno_fora_da_rede A"
				+ "			 JOIN acd_aluno B ON (A.cd_aluno = B.cd_aluno)"
				+ "			 JOIN grl_pessoa C ON (A.cd_aluno = C.cd_pessoa)"
				+ "			 JOIN grl_pessoa_fisica CC ON (A.cd_aluno = CC.cd_pessoa)"
				+ "			 JOIN acd_turma D ON (A.cd_turma = D.cd_turma)"
				+ "			 JOIN acd_curso E ON (D.cd_curso = E.cd_curso)"
				+ "			 JOIN grl_produto_servico F ON (D.cd_curso = F.cd_produto_servico)"
				+ "			 JOIN acd_instituicao G ON (A.cd_instituicao = G.cd_instituicao)"
				+ "			 JOIN grl_pessoa H ON (A.cd_instituicao = H.cd_pessoa)"
				+ "			 JOIN acd_matricula I ON (A.cd_matricula = I.cd_matricula)"
				+ "			 JOIN acd_instituicao_periodo J ON (I.cd_periodo_letivo = J.cd_periodo_letivo)", 
				"		   ORDER BY H.nm_pessoa, F.nm_produto_servico, D.nm_turma, C.nm_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}