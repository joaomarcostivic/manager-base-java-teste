package com.tivic.manager.seg;

import sol.dao.ResultSetMap;
import java.sql.*;

import com.tivic.sol.connection.Conexao;

public class CadastroDAO	{

    public static int insert(Cadastro objeto)    {
        Connection connect = Conexao.conectar();
    	try	{
	        int code = 1;
	        PreparedStatement pstmt = connect.prepareStatement("SELECT MAX(cd_cadastro) FROM SEG_CADASTRO WHERE cd_sistema = ?");
	        pstmt.setInt(1, objeto.getCdSistema());
	        ResultSet rs = pstmt.executeQuery();
	        if(rs.next())
	            code = rs.getInt(1) + 1;
	        pstmt = connect.prepareStatement("INSERT INTO SEG_CADASTRO (CD_CADASTRO,CD_SISTEMA,NM_CADASTRO,ID_CADASTRO) VALUES (?, ?, ?, ?)");
	        pstmt.setInt(1, code);
	        if(objeto.getCdSistema() == 0)
	            pstmt.setNull(2, 4);
	        else
	            pstmt.setInt(2, objeto.getCdSistema());
	        pstmt.setString(3, objeto.getNmCadastro());
	        pstmt.setString(4, objeto.getIdCadastro());
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

    public static int update(Cadastro objeto)    {
        Connection connect = Conexao.conectar();
	    try {
	        PreparedStatement pstmt = connect.prepareStatement("UPDATE SEG_CADASTRO SET NM_CADASTRO=?,ID_CADASTRO=? WHERE CD_CADASTRO=? AND cd_sistema=?");
	        pstmt.setString(1, objeto.getNmCadastro());
	        pstmt.setString(2, objeto.getIdCadastro());
	        pstmt.setInt(3, objeto.getCdCadastro());
	        pstmt.setInt(4, objeto.getCdSistema());
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

    public static int delete(int cdCadastro, int cdSistema)	{
        Connection connect = Conexao.conectar();
    	try {
	        PreparedStatement pstmt = connect.prepareStatement("DELETE FROM SEG_CADASTRO WHERE CD_CADASTRO=?   AND cd_sistema = ?");
	        pstmt.setInt(1, cdCadastro);
	        pstmt.setInt(2, cdSistema);
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

    public static Cadastro get(int cdCadastro, int cdSistema)    {
        Connection connect = Conexao.conectar();
        try	{
	        PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_CADASTRO WHERE CD_CADASTRO=?   AND cd_sistema = ?");
	        pstmt.setInt(1, cdCadastro);
	        pstmt.setInt(2, cdSistema);
	        ResultSet rs = pstmt.executeQuery();
	        if(!rs.next())
	            return null;
	        return new Cadastro(rs.getInt("CD_CADASTRO"), rs.getInt("CD_SISTEMA"), rs.getString("NM_CADASTRO"), rs.getString("ID_CADASTRO"));
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
	        return new ResultSetMap(connect.prepareStatement("SELECT * FROM SEG_CADASTRO").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
    }

    public static ResultSetMap getAllCadastrosOfSistema(int cdSistema)    {
        Connection connect = Conexao.conectar();
        try	{
	        PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_CADASTRO WHERE cd_sistema = "+cdSistema);
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

    public static ResultSetMap findByNmCadastro(int cdSistema, String nmCadastro)	{
        Connection connect = Conexao.conectar();
        try	{
	        PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM SEG_CADASTRO WHERE nm_cadastro LIKE ?   AND cd_sistema = ? ORDER BY nm_cadastro");
	        pstmt.setString(1, (new StringBuilder(String.valueOf(nmCadastro))).append('%').toString());
	        pstmt.setInt(2, cdSistema);
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
}
