package com.tivic.manager.seg;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;

public class AtividadeDAO	{

    public static int insert(Atividade objeto)	{
        Connection connect = Conexao.conectar();
        try	{
	        int code = Conexao.getSequenceCode("SEG_ATIVIDADE");
	        PreparedStatement pstmt = connect.prepareStatement("INSERT INTO SEG_ATIVIDADE (CD_ATIVIDADE,CD_CADASTRO,CD_SISTEMA,NM_ATIVIDADE,ID_ATIVIDADE) VALUES (?, ?, ?, ?, ?)");
	        pstmt.setInt(1, code);
	        if(objeto.getCdCadastro() == 0)
	            pstmt.setNull(2, 4);
	        else
	            pstmt.setInt(2, objeto.getCdCadastro());
	        if(objeto.getCdSistema() == 0)
	            pstmt.setNull(3, 4);
	        else
	            pstmt.setInt(3, objeto.getCdSistema());
	        pstmt.setString(4, objeto.getNmAtividade());
	        pstmt.setString(5, objeto.getIdAtividade());
	        pstmt.executeUpdate();
	        return code;
        }
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
    }

    public static int update(Atividade objeto)	{
        Connection connect = Conexao.conectar();
        try	{
	        PreparedStatement pstmt = connect.prepareStatement("UPDATE SEG_ATIVIDADE SET CD_CADASTRO=?,CD_SISTEMA=?,NM_ATIVIDADE=?,ID_ATIVIDADE=? WHERE CD_ATIVIDADE=?");
	        if(objeto.getCdCadastro() == 0)
	            pstmt.setNull(1, 4);
	        else
	            pstmt.setInt(1, objeto.getCdCadastro());
	        if(objeto.getCdSistema() == 0)
	            pstmt.setNull(2, 4);
	        else
	            pstmt.setInt(2, objeto.getCdSistema());
	        pstmt.setString(3, objeto.getNmAtividade());
	        pstmt.setString(4, objeto.getIdAtividade());
	        pstmt.setInt(5, objeto.getCdAtividade());
	        return pstmt.executeUpdate();
        }
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
    }

    public static int delete(int cdAtividade, int cdCadastro, int cdSistema)    {
        Connection connect = Conexao.conectar();
        try	{
	        PreparedStatement pstmt = connect.prepareStatement("DELETE FROM SEG_ATIVIDADE WHERE CD_ATIVIDADE=?   AND cd_cadastro = ?   AND cd_sistema = ?");
	        pstmt.setInt(1, cdAtividade);
	        pstmt.setInt(2, cdCadastro);
	        pstmt.setInt(3, cdSistema);
	        return pstmt.executeUpdate();
        }
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
    }

    public static Atividade get(int cdAtividade, int cdCadastro, int cdSistema)
    {
        Connection connect = Conexao.conectar();
        try	{
	        PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_ATIVIDADE WHERE CD_ATIVIDADE=?   AND cd_cadastro = ?   AND cd_sistema = ?");
	        pstmt.setInt(1, cdAtividade);
	        pstmt.setInt(2, cdCadastro);
	        pstmt.setInt(3, cdSistema);
	        ResultSet rs = pstmt.executeQuery();
	        if(!rs.next())
	            return null;
	        return new Atividade(rs.getInt("CD_ATIVIDADE"), rs.getInt("CD_CADASTRO"), rs.getInt("CD_SISTEMA"), rs.getString("NM_ATIVIDADE"), rs.getString("ID_ATIVIDADE"));
        }
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
    }

    public static ResultSetMap getAll()    {
        Connection connect = Conexao.conectar();
        try	{
	        PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_ATIVIDADE");
	        return new ResultSetMap(pstmt.executeQuery());
        }
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
    }

    public static ResultSetMap find(ArrayList<ItemComparator> criterios)    {
        return Search.find("SELECT A.*, B.nm_cadastro FROM seg_atividade A LEFT OUTER JOIN seg_cadastro B ON (A.cd_cadastro = B.cd_cadastro) ", criterios, Conexao.conectar());
    }
}
