package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.ptc.DocumentoPessoaServices;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AtendimentoDocumentoServices {

	public static Result save(AtendimentoDocumento atendimentoDocumento){
		return save(atendimentoDocumento, null, null);
	}

	public static Result save(AtendimentoDocumento atendimentoDocumento, AuthData authData){
		return save(atendimentoDocumento, authData, null);
	}

	public static Result save(AtendimentoDocumento atendimentoDocumento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(atendimentoDocumento==null)
				return new Result(-1, "Erro ao salvar. AtendimentoDocumento é nulo");

			int retorno;
			if(atendimentoDocumento.getCdConcessao()==0){
				retorno = AtendimentoDocumentoDAO.insert(atendimentoDocumento, connect);
				atendimentoDocumento.setCdConcessao(retorno);
			}
			else {
				retorno = AtendimentoDocumentoDAO.update(atendimentoDocumento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ATENDIMENTODOCUMENTO", atendimentoDocumento);
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
	public static Result remove(AtendimentoDocumento atendimentoDocumento) {
		return remove(atendimentoDocumento.getCdConcessao(), atendimentoDocumento.getCdDocumento());
	}
	public static Result remove(int cdConcessao, int cdDocumento){
		return remove(cdConcessao, cdDocumento, false, null, null);
	}
	public static Result remove(int cdConcessao, int cdDocumento, boolean cascade){
		return remove(cdConcessao, cdDocumento, cascade, null, null);
	}
	public static Result remove(int cdConcessao, int cdDocumento, boolean cascade, AuthData authData){
		return remove(cdConcessao, cdDocumento, cascade, authData, null);
	}
	public static Result remove(int cdConcessao, int cdDocumento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AtendimentoDocumentoDAO.delete(cdConcessao, cdDocumento, connect);
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
	
	public static Result removeByDocumento(int cdDocumento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_atendimento_documento WHERE cd_documento = "+cdDocumento);
			
			retorno = pstmt.executeUpdate();
			
			
			if(retorno<0){
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_atendimento_documento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByDocumento(int cdDocumento) {
		return getAllByDocumento(cdDocumento, null);
	}

	public static ResultSetMap getAllByDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_atendimento_documento WHERE cd_documento = "+cdDocumento);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoServices.getAllByDocumento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoServices.getAllByDocumento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getEnvolvidos(int cdDocumento) {
		return getEnvolvidos(cdDocumento, null);
	}

	public static ResultSetMap getEnvolvidos(int cdDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			ResultSetMap rsmEnvolvidos = DocumentoPessoaServices.getAllByDocumento(cdDocumento);
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			// Reclamante
			if (rsmEnvolvidos.next()) {
				
				criterios.add(new ItemComparator("A.CD_PESSOA", Integer.toString(rsmEnvolvidos.getInt("CD_PESSOA")), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("findEnderecoPrincipal", "true", ItemComparator.EQUAL, Types.BOOLEAN));
				ResultSetMap rsmReclamante = PessoaServices.find(criterios, connect);
				//System.out.println("RSM = " + rsmReclamante);
				
				if (rsmReclamante.next()) {
					rsmEnvolvidos.setValueToField("NR_CPF", rsmReclamante.getInt("NR_CPF"));
					rsmEnvolvidos.setValueToField("NM_LOGRADOURO", rsmReclamante.getString("NM_LOGRADOURO"));
					rsmEnvolvidos.setValueToField("NR_CEP", rsmReclamante.getString("NR_CEP"));
					rsmEnvolvidos.setValueToField("NR_CELULAR", rsmReclamante.getString("NR_CELULAR"));
					rsmEnvolvidos.setValueToField("NM_BAIRRO", rsmReclamante.getString("NM_BAIRRO"));
					rsmEnvolvidos.setValueToField("NM_CIDADE", rsmReclamante.getString("NM_CIDADE"));
				}
			}
			
			// Reclamado
			pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa FROM mob_atendimento_documento A" +
											" LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao)" +
											" LEFT OUTER JOIN grl_pessoa C ON (B.cd_concessionario = C.cd_pessoa)" +
											" WHERE A.cd_documento = ?");
			pstmt.setInt(1, cdDocumento);
			ResultSetMap rsmReclamado = new ResultSetMap(pstmt.executeQuery());
			
			if(rsmReclamado.next()) {
				
				if (rsmReclamado.getInt("CD_LINHA")!=0) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_LINHA", Integer.toString(rsmReclamado.getInt("CD_LINHA")), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.CD_ROTA", Integer.toString(rsmReclamado.getInt("CD_ROTA")), ItemComparator.EQUAL, Types.INTEGER));
					
					ResultSetMap rsmLinha = LinhaRotaServices.find(criterios, connect);
					if(rsmLinha.next()) {
						rsmReclamado.setValueToField("NM_ROTA", rsmLinha.getString("DS_ROTA"));
						rsmReclamado.setValueToField("NM_CONCESSIONARIO", rsmLinha.getString("NM_PESSOA"));
						rsmReclamado.setValueToField("NR_LINHA", rsmLinha.getString("NR_LINHA"));
						rsmReclamado.setValueToField("NM_QUALIFICACAO", "Reclamado");
					}
									
				} 
				if (rsmReclamado.getInt("CD_VEICULO")!=0) {
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_VEICULO", Integer.toString(rsmReclamado.getInt("CD_VEICULO")), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.CD_CONCESSAO", Integer.toString(rsmReclamado.getInt("CD_CONCESSAO")), ItemComparator.EQUAL, Types.INTEGER));
					
					ResultSetMap rsmConcessaoVeiculo = ConcessaoVeiculoServices.find(criterios, connect);
					if(rsmConcessaoVeiculo.next()) {
						rsmReclamado.setValueToField("NM_TIPO_VEICULO", rsmConcessaoVeiculo.getString("NM_TIPO_VEICULO"));
						rsmReclamado.setValueToField("NM_MODELO", rsmConcessaoVeiculo.getString("NM_MODELO"));
						rsmReclamado.setValueToField("NR_PLACA", rsmConcessaoVeiculo.getString("NR_PLACA"));
						rsmReclamado.setValueToField("NR_PREFIXO", rsmConcessaoVeiculo.getString("NR_PREFIXO"));
					}
				}
				
				rsmEnvolvidos.addRegister(rsmReclamado.getRegister());
			}
			rsmEnvolvidos.beforeFirst();
			
			return rsmEnvolvidos;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoServices.getEnvolvidos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDocumentoServices.getEnvolvidos: " + e);
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
		return Search.find("SELECT * FROM mob_atendimento_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}