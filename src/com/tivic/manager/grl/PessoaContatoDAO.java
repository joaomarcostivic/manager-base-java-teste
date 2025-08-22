package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class PessoaContatoDAO{

	public static int insert(PessoaContato objeto) {
		return insert(objeto, null);
	}

	public static int insert(PessoaContato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_pessoa_contato");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_pessoa");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			int code = Conexao.getSequenceCode("grl_pessoa_contato", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPessoaContato(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_contato (cd_pessoa_contato,"+
			                                  "cd_pessoa,"+
			                                  "nm_pessoa_contato,"+
			                                  "nm_apelido,"+
			                                  "nm_setor,"+
			                                  "nr_telefone1,"+
			                                  "nr_telefone2,"+
			                                  "nr_celular,"+
			                                  "nm_email,"+
			                                  "nm_url,"+
			                                  "dt_cadastro,"+
			                                  "txt_observacao,"+
			                                  "cd_funcao,"+
			                                  "cd_endereco,"+
			                                  "cd_usuario,"+
			                                  "nr_celular2,"+
			                                  "nr_celular3,"+
			                                  "nr_celular4,"+
			                                  "lg_email_principal,"+
			                                  "lg_email_extrato,"+
			                                  "lg_email_boleto,"+
			                                  "lg_email_cobranca,"+
			                                  "lg_email_nfe,"+
			                                  "nm_servidor_smtp,"+
			                                  "nm_servidor_pop,"+
			                                  "tp_telefone1,"+
			                                  "tp_telefone2,"+
			                                  "nr_telefone1_ramal,"+
			                                  "nr_telefone2_ramal,"+
			                                  "nr_fax_ramal,"+
			                                  "tp_celular_operadora,"+
			                                  "tp_celular2_operadora,"+
			                                  "tp_celular3_operadora,"+
			                                  "tp_celular4_operadora,"+
			                                  "lg_sms_celular,"+
			                                  "lg_sms_celular2,"+
			                                  "lg_sms_celular3,"+
			                                  "lg_sms_celular4,"+
			                                  "nm_turno,"+
			                                  "nm_skype,"+
			                                  "nm_tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNmPessoaContato());
			pstmt.setString(4,objeto.getNmApelido());
			pstmt.setString(5,objeto.getNmSetor());
			pstmt.setString(6,objeto.getNrTelefone1());
			pstmt.setString(7,objeto.getNrTelefone2());
			pstmt.setString(8,objeto.getNrCelular());
			pstmt.setString(9,objeto.getNmEmail());
			pstmt.setString(10,objeto.getNmUrl());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(12,objeto.getTxtObservacao());
			if(objeto.getCdFuncao()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdFuncao());
			if(objeto.getCdEndereco()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdEndereco());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdUsuario());
			pstmt.setString(16,objeto.getNrCelular2());
			pstmt.setString(17,objeto.getNrCelular3());
			pstmt.setString(18,objeto.getNrCelular4());
			pstmt.setInt(19,objeto.getLgEmailPrincipal());
			pstmt.setInt(20,objeto.getLgEmailExtrato());
			pstmt.setInt(21,objeto.getLgEmailBoleto());
			pstmt.setInt(22,objeto.getLgEmailCobranca());
			pstmt.setInt(23,objeto.getLgEmailNfe());
			pstmt.setString(24,objeto.getNmServidorSmtp());
			pstmt.setString(25,objeto.getNmServidorPop());
			pstmt.setInt(26,objeto.getTpTelefone1());
			pstmt.setInt(27,objeto.getTpTelefone2());
			pstmt.setInt(28,objeto.getNrTelefone1Ramal());
			pstmt.setInt(29,objeto.getNrTelefone2Ramal());
			pstmt.setInt(30,objeto.getNrFaxRamal());
			pstmt.setInt(31,objeto.getTpCelularOperadora());
			pstmt.setInt(32,objeto.getTpCelular2Operadora());
			pstmt.setInt(33,objeto.getTpCelular3Operadora());
			pstmt.setInt(34,objeto.getTpCelular4Operadora());
			pstmt.setInt(35,objeto.getLgSmsCelular());
			pstmt.setInt(36,objeto.getLgSmsCelular2());
			pstmt.setInt(37,objeto.getLgSmsCelular3());
			pstmt.setInt(38,objeto.getLgSmsCelular4());
			pstmt.setString(39,objeto.getNmTurno());
			pstmt.setString(40,objeto.getNmSkype());
			pstmt.setString(41,objeto.getNmTipo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PessoaContato objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PessoaContato objeto, int cdPessoaContatoOld, int cdPessoaOld) {
		return update(objeto, cdPessoaContatoOld, cdPessoaOld, null);
	}

	public static int update(PessoaContato objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PessoaContato objeto, int cdPessoaContatoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_pessoa_contato SET cd_pessoa_contato=?,"+
												      		   "cd_pessoa=?,"+
												      		   "nm_pessoa_contato=?,"+
												      		   "nm_apelido=?,"+
												      		   "nm_setor=?,"+
												      		   "nr_telefone1=?,"+
												      		   "nr_telefone2=?,"+
												      		   "nr_celular=?,"+
												      		   "nm_email=?,"+
												      		   "nm_url=?,"+
												      		   "dt_cadastro=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_funcao=?,"+
												      		   "cd_endereco=?,"+
												      		   "cd_usuario=?,"+
												      		   "nr_celular2=?,"+
												      		   "nr_celular3=?,"+
												      		   "nr_celular4=?,"+
												      		   "lg_email_principal=?,"+
												      		   "lg_email_extrato=?,"+
												      		   "lg_email_boleto=?,"+
												      		   "lg_email_cobranca=?,"+
												      		   "lg_email_nfe=?,"+
												      		   "nm_servidor_smtp=?,"+
												      		   "nm_servidor_pop=?,"+
												      		   "tp_telefone1=?,"+
												      		   "tp_telefone2=?,"+
												      		   "nr_telefone1_ramal=?,"+
												      		   "nr_telefone2_ramal=?,"+
												      		   "nr_fax_ramal=?,"+
												      		   "tp_celular_operadora=?,"+
												      		   "tp_celular2_operadora=?,"+
												      		   "tp_celular3_operadora=?,"+
												      		   "tp_celular4_operadora=?,"+
												      		   "lg_sms_celular=?,"+
												      		   "lg_sms_celular2=?,"+
												      		   "lg_sms_celular3=?,"+
												      		   "lg_sms_celular4=?,"+
												      		   "nm_turno=?,"+
												      		   "nm_skype=?,"+
												      		   "nm_tipo=? WHERE cd_pessoa_contato=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdPessoaContato());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNmPessoaContato());
			pstmt.setString(4,objeto.getNmApelido());
			pstmt.setString(5,objeto.getNmSetor());
			pstmt.setString(6,objeto.getNrTelefone1());
			pstmt.setString(7,objeto.getNrTelefone2());
			pstmt.setString(8,objeto.getNrCelular());
			pstmt.setString(9,objeto.getNmEmail());
			pstmt.setString(10,objeto.getNmUrl());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(12,objeto.getTxtObservacao());
			if(objeto.getCdFuncao()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdFuncao());
			if(objeto.getCdEndereco()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdEndereco());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdUsuario());
			pstmt.setString(16,objeto.getNrCelular2());
			pstmt.setString(17,objeto.getNrCelular3());
			pstmt.setString(18,objeto.getNrCelular4());
			pstmt.setInt(19,objeto.getLgEmailPrincipal());
			pstmt.setInt(20,objeto.getLgEmailExtrato());
			pstmt.setInt(21,objeto.getLgEmailBoleto());
			pstmt.setInt(22,objeto.getLgEmailCobranca());
			pstmt.setInt(23,objeto.getLgEmailNfe());
			pstmt.setString(24,objeto.getNmServidorSmtp());
			pstmt.setString(25,objeto.getNmServidorPop());
			pstmt.setInt(26,objeto.getTpTelefone1());
			pstmt.setInt(27,objeto.getTpTelefone2());
			pstmt.setInt(28,objeto.getNrTelefone1Ramal());
			pstmt.setInt(29,objeto.getNrTelefone2Ramal());
			pstmt.setInt(30,objeto.getNrFaxRamal());
			pstmt.setInt(31,objeto.getTpCelularOperadora());
			pstmt.setInt(32,objeto.getTpCelular2Operadora());
			pstmt.setInt(33,objeto.getTpCelular3Operadora());
			pstmt.setInt(34,objeto.getTpCelular4Operadora());
			pstmt.setInt(35,objeto.getLgSmsCelular());
			pstmt.setInt(36,objeto.getLgSmsCelular2());
			pstmt.setInt(37,objeto.getLgSmsCelular3());
			pstmt.setInt(38,objeto.getLgSmsCelular4());
			pstmt.setString(39,objeto.getNmTurno());
			pstmt.setString(40,objeto.getNmSkype());
			pstmt.setString(41,objeto.getNmTipo());
			pstmt.setInt(42, cdPessoaContatoOld!=0 ? cdPessoaContatoOld : objeto.getCdPessoaContato());
			pstmt.setInt(43, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoaContato, int cdPessoa) {
		return delete(cdPessoaContato, cdPessoa, null);
	}

	public static int delete(int cdPessoaContato, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_pessoa_contato WHERE cd_pessoa_contato=? AND cd_pessoa=?");
			pstmt.setInt(1, cdPessoaContato);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PessoaContato get(int cdPessoaContato, int cdPessoa) {
		return get(cdPessoaContato, cdPessoa, null);
	}

	public static PessoaContato get(int cdPessoaContato, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_contato WHERE cd_pessoa_contato=? AND cd_pessoa=?");
			pstmt.setInt(1, cdPessoaContato);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PessoaContato(rs.getInt("cd_pessoa_contato"),
						rs.getInt("cd_pessoa"),
						rs.getString("nm_pessoa_contato"),
						rs.getString("nm_apelido"),
						rs.getString("nm_setor"),
						rs.getString("nr_telefone1"),
						rs.getString("nr_telefone2"),
						rs.getString("nr_celular"),
						rs.getString("nm_email"),
						rs.getString("nm_url"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getString("txt_observacao"),
						rs.getInt("cd_funcao"),
						rs.getInt("cd_endereco"),
						rs.getInt("cd_usuario"),
						rs.getString("nr_celular2"),
						rs.getString("nr_celular3"),
						rs.getString("nr_celular4"),
						rs.getInt("lg_email_principal"),
						rs.getInt("lg_email_extrato"),
						rs.getInt("lg_email_boleto"),
						rs.getInt("lg_email_cobranca"),
						rs.getInt("lg_email_nfe"),
						rs.getString("nm_servidor_smtp"),
						rs.getString("nm_servidor_pop"),
						rs.getInt("tp_telefone1"),
						rs.getInt("tp_telefone2"),
						rs.getInt("nr_telefone1_ramal"),
						rs.getInt("nr_telefone2_ramal"),
						rs.getInt("nr_fax_ramal"),
						rs.getInt("tp_celular_operadora"),
						rs.getInt("tp_celular2_operadora"),
						rs.getInt("tp_celular3_operadora"),
						rs.getInt("tp_celular4_operadora"),
						rs.getInt("lg_sms_celular"),
						rs.getInt("lg_sms_celular2"),
						rs.getInt("lg_sms_celular3"),
						rs.getInt("lg_sms_celular4"),
						rs.getString("nm_turno"),
						rs.getString("nm_skype"),
						rs.getString("nm_tipo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_contato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PessoaContato> getList() {
		return getList(null);
	}

	public static ArrayList<PessoaContato> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PessoaContato> list = new ArrayList<PessoaContato>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PessoaContato obj = PessoaContatoDAO.get(rsm.getInt("cd_pessoa_contato"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaContatoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_pessoa_contato", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
