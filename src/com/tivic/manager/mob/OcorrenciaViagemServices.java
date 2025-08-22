package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.acd.CursoServices;
import com.tivic.manager.acd.InstituicaoEducacensoServices;
import com.tivic.manager.acd.InstituicaoPeriodo;
import com.tivic.manager.acd.InstituicaoPeriodoDAO;
import com.tivic.manager.acd.InstituicaoPeriodoServices;
import com.tivic.manager.acd.InstituicaoServices;
import com.tivic.manager.acd.MatriculaServices;
import com.tivic.manager.acd.TurmaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaTipoDocumentacaoServices;
import com.tivic.manager.grl.TipoOcorrenciaDAO;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class OcorrenciaViagemServices {

	public static Result save(OcorrenciaViagem ocorrenciaViagem){
		return save(ocorrenciaViagem, null, null);
	}

	public static Result save(OcorrenciaViagem ocorrenciaViagem, AuthData authData){
		return save(ocorrenciaViagem, authData, null);
	}

	public static Result save(OcorrenciaViagem ocorrenciaViagem, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrenciaViagem==null)
				return new Result(-1, "Erro ao salvar. OcorrenciaViagem é nulo");

			int retorno;
			if(ocorrenciaViagem.getCdOcorrencia()==0){
				retorno = OcorrenciaViagemDAO.insert(ocorrenciaViagem, connect);
				ocorrenciaViagem.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaViagemDAO.update(ocorrenciaViagem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIAVIAGEM", ocorrenciaViagem);
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
	public static Result remove(OcorrenciaViagem ocorrenciaViagem) {
		return remove(ocorrenciaViagem.getCdOcorrencia());
	}
	public static Result remove(int cdOcorrencia){
		return remove(cdOcorrencia, false, null, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade){
		return remove(cdOcorrencia, cascade, null, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, AuthData authData){
		return remove(cdOcorrencia, cascade, authData, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OcorrenciaViagemDAO.delete(cdOcorrencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ocorrencia_viagem");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaViagemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ocorrencia_viagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result findRelatorio(ArrayList<ItemComparator> criterios) {
		return findRelatorio(criterios, null);
	}

	public static Result findRelatorio(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int qtLimite = 0;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else
					crt.add(criterios.get(i));
				
			}
			
			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
			String sql =
		 			  "SELECT "+sqlLimit[0]+" A.*, B.*, C.*, E.nm_pessoa AS NM_INSTITUICAO, F.NM_SETOR, G.NR_PREFIXO, I.NM_MARCA, I.NM_MODELO, J.nm_pessoa AS NM_MOTORISTA, J.nr_telefone1, J.nr_telefone2, J.nr_celular, J.nr_celular2 " + 
 					  "FROM mob_ocorrencia_viagem A " +
 					  "LEFT OUTER JOIN grl_ocorrencia 		 B ON (A.cd_ocorrencia = B.cd_ocorrencia) " +
 					  "LEFT OUTER JOIN mob_viagem		  	 C ON (A.cd_viagem_posterior = C.cd_viagem) " +
 					  "LEFT OUTER JOIN acd_instituicao 		 D ON (C.cd_instituicao = D.cd_instituicao) " +
 					  "LEFT OUTER JOIN grl_pessoa 			 E ON (D.cd_instituicao = E.cd_pessoa) " +
 					  "LEFT OUTER JOIN grl_setor 			 F ON (C.cd_setor = F.cd_setor) " +
					  "LEFT OUTER JOIN mob_concessao_veiculo G ON (C.cd_concessao_veiculo = G.cd_concessao_veiculo) " +
 					  "LEFT OUTER JOIN fta_veiculo		 	 H ON (G.cd_veiculo = H.cd_veiculo) " +
 					  "LEFT OUTER JOIN fta_marca_modelo	 	 I ON (H.cd_marca = I.cd_marca) " +
 					  "LEFT OUTER JOIN grl_pessoa   	     J ON (C.cd_motorista = J.cd_pessoa) " +
 					  " WHERE 1=1 ";
			
			ResultSetMap rsm = Search.find(sql, " "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()){
				rsm.setValueToField("ID_VIAGEM", rsm.getString("cd_viagem"));
				rsm.setValueToField("CL_SITUACAO_VIAGEM", ViagemServices.situacaoViagem[rsm.getInt("st_viagem")]);
				rsm.setValueToField("NM_DT_OCORRENCIA", Util.convCalendarStringCompleto(rsm.getGregorianCalendar("dt_ocorrencia")));
				rsm.setValueToField("NM_TP_OCORRENCIA", TipoOcorrenciaDAO.get(rsm.getInt("cd_tipo_ocorrencia"), connect).getNmTipoOcorrencia());
				rsm.setValueToField("NM_DT_VIAGEM", Util.convCalendarStringCompleto(rsm.getGregorianCalendar("dt_ocorrencia")));
				rsm.setValueToField("CL_PARTIDA", Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_partida")));
				rsm.setValueToField("CL_CHEGADA", Util.convCalendarStringHourMinute(rsm.getGregorianCalendar("hr_chegada")));
				
				rsm.setValueToField("NM_VEICULO_COMPLETO", "E" + rsm.getString("NR_PREFIXO") + " - " + rsm.getString("NM_MARCA") + "/" + rsm.getString("NM_MODELO"));
				
				
				
				String[] telefones = new String[3];
				int i = 0;
				
				if(rsm.getString("NR_TELEFONE1") != null && !rsm.getString("NR_TELEFONE1").trim().equals(""))
					telefones[i++] = rsm.getString("NR_TELEFONE1");
				
				if(rsm.getString("NR_TELEFONE2") != null && !rsm.getString("NR_TELEFONE2").trim().equals(""))
					telefones[i++] = rsm.getString("NR_TELEFONE2");
				
				if(rsm.getString("NR_CELULAR") != null && !rsm.getString("NR_CELULAR").trim().equals(""))
					telefones[i++] = rsm.getString("NR_CELULAR");
				
				String nrTelefones = "";
				for(String telefone : telefones){
					nrTelefones += (telefone != null ? telefone + " / " : "");
				}
				if(nrTelefones.equals("")){
					nrTelefones = "Não possui telefone cadastrado";
				}
				else{
					nrTelefones = nrTelefones.substring(0, nrTelefones.length()-3);
				}
				
				rsm.setValueToField("NR_TELEFONES", nrTelefones);
				
				
				rsm.setValueToField("NR_TELEFONE", (rsm.getString("NR_TELEFONE1") != null && !rsm.getString("NR_TELEFONE1").trim().equals("") ? rsm.getString("NR_TELEFONE1") : (rsm.getString("NR_TELEFONE2") != null && !rsm.getString("NR_TELEFONE2").trim().equals("") ? rsm.getString("NR_TELEFONE2") : (rsm.getString("NR_CELULAR") != null && !rsm.getString("NR_CELULAR").trim().equals("") ? rsm.getString("NR_CELULAR") : rsm.getString("NR_TELEFONE")))));
				
			}
			rsm.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_OCORRENCIA DESC");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			
			return result;
		}
		catch(Exception e) {		
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}