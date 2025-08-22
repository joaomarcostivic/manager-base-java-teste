package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.Util;

public class OcorrenciaQuadroVagasServices {

	public static final int ST_RESPONDIDO_ESCOLA = 0;
	public static final int ST_RESPONDIDO_SMED   = 1;
	public static final int ST_FECHADO           = 2;

	public static final String[] situacaoOcorrencia = {"Pendente", "Respondido"};
	
	public static Result saveFechamento(int cdOcorrenciaQuadroVagas){
		return saveFechamento(cdOcorrenciaQuadroVagas, null);
	}
	
	public static Result saveFechamento(int cdOcorrenciaQuadroVagas, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
						
			OcorrenciaQuadroVagas ocorrenciaQuadroVagas = OcorrenciaQuadroVagasDAO.get(cdOcorrenciaQuadroVagas, connect);
			
			ocorrenciaQuadroVagas.setStOcorrencia(ST_FECHADO);
			if(OcorrenciaQuadroVagasDAO.update(ocorrenciaQuadroVagas, connect) <= 0){
				return new Result(-1, "Erro ao fechar conversa");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Fechamento realizado com sucesso");
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro no fechamento de convesa");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result save(OcorrenciaQuadroVagas ocorrenciaQuadroVagas, int cdQuadroVagas, int cdInstituicao, String txtNovaMensagem, int tpMensagem, boolean lgFechamento){
		return save(ocorrenciaQuadroVagas, cdQuadroVagas, cdInstituicao, txtNovaMensagem, tpMensagem, lgFechamento, null);
	}

	public static Result save(OcorrenciaQuadroVagas ocorrenciaQuadroVagas, int cdQuadroVagas, int cdInstituicao, String txtNovaMensagem, int tpMensagem, boolean lgFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrenciaQuadroVagas==null)
				return new Result(-1, "Erro ao salvar. OcorrenciaQuadroVagas é nulo");

			ocorrenciaQuadroVagas.setCdTipoOcorrencia(TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_QUADRO_VAGAS, connect).getCdTipoOcorrencia());
			
			if(ocorrenciaQuadroVagas.getCdOcorrencia() > 0)
				ocorrenciaQuadroVagas.setTxtOcorrencia(OcorrenciaQuadroVagasDAO.get(ocorrenciaQuadroVagas.getCdOcorrencia(), connect).getTxtOcorrencia());
			
			if(tpMensagem == 0)
				ocorrenciaQuadroVagas.setStOcorrencia(ST_RESPONDIDO_SMED);
			else
				ocorrenciaQuadroVagas.setStOcorrencia(ST_RESPONDIDO_ESCOLA);
			
			String txtOcorrencia = ocorrenciaQuadroVagas.getTxtOcorrencia();
			
			if(txtOcorrencia != null && txtOcorrencia.trim().length() > 0){
				txtOcorrencia += "\n";
			}
			else{
				txtOcorrencia = "";
			}
			
			Pessoa pessoaUsuario = PessoaDAO.get(ocorrenciaQuadroVagas.getCdPessoa(), connect);
			Pessoa pessoaUsuarioSmed = PessoaDAO.get(ocorrenciaQuadroVagas.getCdPessoaResposta(), connect);
			
			txtOcorrencia += "(" + Util.convCalendarStringCompleto(Util.getDataAtual()) + ") " + (tpMensagem == 0 ? pessoaUsuarioSmed.getNmPessoa() : pessoaUsuario.getNmPessoa()) + " diz:\n" + txtNovaMensagem+"\n";
			
			ocorrenciaQuadroVagas.setTxtOcorrencia(txtOcorrencia);
			
			
			int retorno;
			if(ocorrenciaQuadroVagas.getCdOcorrencia()==0){
				retorno = OcorrenciaQuadroVagasDAO.insert(ocorrenciaQuadroVagas, connect);
				ocorrenciaQuadroVagas.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaQuadroVagasDAO.update(ocorrenciaQuadroVagas, connect);
			}
			
			
			if(QuadroVagasOcorrenciaServices.save(new QuadroVagasOcorrencia(cdQuadroVagas, cdInstituicao, ocorrenciaQuadroVagas.getCdOcorrencia()), connect).getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao salvar relação com quadro de vagas");
			}

			
			
			if(lgFechamento){
				retorno = saveFechamento(ocorrenciaQuadroVagas.getCdOcorrencia(), connect).getCode();
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIAQUADROVAGAS", ocorrenciaQuadroVagas);
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
	public static Result remove(int cdOcorrencia){
		return remove(cdOcorrencia, false, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade){
		return remove(cdOcorrencia, cascade, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, Connection connect){
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
			retorno = OcorrenciaQuadroVagasDAO.delete(cdOcorrencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_quadro_vagas");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaQuadroVagasServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaQuadroVagasServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_ocorrencia_quadro_vagas", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
