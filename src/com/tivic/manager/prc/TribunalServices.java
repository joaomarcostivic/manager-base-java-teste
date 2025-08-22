package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.crt.PessoaServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaServices;
import com.tivic.manager.util.Util;

public class TribunalServices {
	public static Result save(Tribunal tribunal){
		return save(tribunal, null);
	}
	
	public static Result save(Tribunal tribunal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tribunal==null)
				return new Result(-1, "Erro ao salvar. Tribunal é nulo");
			
			int retorno;
			if(tribunal.getCdTribunal()==0){
				retorno = TribunalDAO.insert(tribunal, connect);
				tribunal.setCdTribunal(retorno);
			}
			else {
				retorno = TribunalDAO.update(tribunal, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TRIBUNAL", tribunal);
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
	
	public static Result remove(int cdTribunal){
		return remove(cdTribunal, false, null);
	}
	
	public static Result remove(int cdTribunal, boolean cascade){
		return remove(cdTribunal, cascade, null);
	}
	
	public static Result remove(int cdTribunal, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_tribunal_cidade " +
						" WHERE cd_tribunal = ? ");
				pstmt.setInt(1, cdTribunal);
				
				retorno = pstmt.executeUpdate();
			}
				
			if(!cascade || retorno>0)
				retorno = TribunalDAO.delete(cdTribunal, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tribunal está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tribunal excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tribunal!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tribunal ORDER BY nm_tribunal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Cidade getCidadeByTribunalOrigem(int cdTribunal, String nrOrigem) {
		return getCidadeByTribunalOrigem(cdTribunal, nrOrigem, null);
	}
	
	public static Cidade getCidadeByTribunalOrigem(int cdTribunal, String nrOrigem, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = TribunalCidadeServices.getAllByTribunal(cdTribunal, nrOrigem, connect);
			Cidade cidade = null;
			if(rsm.next())
				cidade = CidadeDAO.get(rsm.getInt("CD_CIDADE"), connect);
			return cidade;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sincronizarPessoaJuridica(int cdTribunal){
		return sincronizarPessoaJuridica(cdTribunal, null);
	}
	
	public static Result sincronizarPessoaJuridica(int cdTribunal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Tribunal tribunal = TribunalDAO.get(cdTribunal, connect);
			
			if(tribunal==null)
				return new Result(-1, "Erro ao gerar pessoa jurídica para o tribunal indicado. Tribunal é nulo.");
			
			int retorno = 0;
			
			PessoaJuridica pessoaJuridica = new PessoaJuridica(0, 0, 0, tribunal.getNmTribunal(), null, null, null, null, null, 
					new GregorianCalendar(), PessoaServices.JURIDICA, null, 1, null, null, null, 0, tribunal.getSgTribunal(), 
					0, 0, null, null, tribunal.getNmTribunal(), null, null, 0, null, 0, 4, null);
			
			retorno = PessoaJuridicaServices.save(pessoaJuridica, null, connect).getCode();
			pessoaJuridica.setCdPessoa(retorno);
			
			if(retorno>0) {
				PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_tribunal SET CD_PESSOA_JURIDICA = ? WHERE cd_tribunal=?");
				pstmt.setInt(1, pessoaJuridica.getCdPessoa());
				pstmt.setInt(2, tribunal.getCdTribunal());
				pstmt.executeUpdate();
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao gerar pessoa jurídica para o tribunal indicado.":"Tribunal sincronizado com sucesso...", "PESSOAJURIDICA", pessoaJuridica);
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
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		
		String nmTribunal = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("nm_tribunal")) {
				nmTribunal = Util.limparTexto(criterios.get(i).getValue());
				nmTribunal = "%"+nmTribunal.replaceAll(" ", "%")+"%";
				criterios.remove(i);
			}
		}
		
		String sql = "SELECT * FROM prc_tribunal"
				   + " WHERE 1=1"
				   + (Util.getConfManager().getIdOfDbUsed().equals("FB") ? 
						   (!nmTribunal.equals("") ? " AND nm_tribunal COLLATE PT_BR LIKE '"+nmTribunal+"'" : "") :
						   (!nmTribunal.equals("") ? " AND TRANSLATE(nm_tribunal, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '"+nmTribunal+"' " : ""));
		
		return Search.find(sql, " ORDER BY nm_tribunal ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getTribunalAgrupado(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT C.NM_TRIBUNAL AS NM_AGRUPAMENTO, C.SG_TRIBUNAL, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO      A " + 
					"JOIN PRC_TRIBUNAL  C ON (C.CD_TRIBUNAL = A.CD_TRIBUNAL) " +
					"WHERE A.ST_PROCESSO = " + ProcessoServices.ST_PROCESSO_ATIVO +
					(processos.size()>0 ? " AND A.cd_processo IN ("+Util.join(processos)+")" : "") +
					"GROUP BY C.CD_TRIBUNAL, C.NM_TRIBUNAL, C.SG_TRIBUNAL " +
					"ORDER BY QT_PROCESSOS DESC, C.NM_TRIBUNAL "
					);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrgaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}