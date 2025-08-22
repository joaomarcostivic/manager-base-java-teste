package com.tivic.manager.str;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.NoContentException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.str.ait.veiculo.ConvertPlaca;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.auth.jwt.JWT;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AuthPortalService {
	
	public String authEdat() throws Exception {
		int expTime = 12;
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			int cdUsuario = ParametroServices.getValorOfParametroAsInteger("MOB_USER_EDAT", 0);
			if(cdUsuario <= 0) {
				throw new Exception("Parâmetro de usuário não configurado, contate o órgão.");
			}
			String login = getNmLoginByCdUsuario(cdUsuario, customConnection);
			customConnection.finishConnection();
			String token = generateToken(expTime, login);
			return token;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public String authPortal(String nrPlaca, String nrRenavan) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	        customConnection.initConnection(false);
	        Ait ait = new Ait();
	        String token = null;
	        String nrPlacaConvertida = ConvertPlaca.convertPlaca(nrPlaca);
	        ait = getAit(nrPlaca, nrPlacaConvertida, nrRenavan, customConnection);
	        if (ait != null) {
	            int expTime = 60;
	            int cdUsuario = ParametroServices.getValorOfParametroAsInteger("MOB_USER_PORTAL", 0);
	            if(cdUsuario <= 0) {
	                throw new ValidacaoException("Parâmetro de usuário não configurado, contate o órgão.");
	            }
	            String nmLogin = getNmLoginByCdUsuario(cdUsuario, customConnection);
	            token = generateToken(expTime, nmLogin);
	        }
	        customConnection.finishConnection();
	        return token;
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	public Ait getAit(String nrPlaca, String nrPlacaConvertida, String nrRenavan, CustomConnection customConnection) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualString("cd_renavan", nrRenavan);
	    Search<Ait> search = new SearchBuilder<Ait>("ait")
                .fields("nr_placa, cd_renavan")
                .searchCriterios(searchCriterios)
                .additionalCriterias("nr_placa IN (" + "'" + nrPlaca + "'" + ", " + "'" + nrPlacaConvertida + "'" + ")")
                .customConnection(customConnection)
                .build();
	    return search.getList(Ait.class).get(0);
	}
	
	private String getNmLoginByCdUsuario(int cdUsuario, CustomConnection customConnection) throws Exception {
        SearchCriterios searchCriterios = new SearchCriterios();
        searchCriterios.addCriteriosEqualInteger("cd_usuario", cdUsuario);
        Search<Usuario> search = new SearchBuilder<Usuario>("seg_usuario A")
                .fields("A.nm_login")
                .searchCriterios(searchCriterios)
                .customConnection(customConnection)
                .build();
        List<Usuario> usuarios = search.getList(Usuario.class);
        if (usuarios.isEmpty()) {
            throw new NoContentException("Nenhum usuário encontrado.");
        }
        return usuarios.get(0).getNmLogin();
    }
	
	private String generateToken(int expTime, String nmLogin) throws Exception {
		String token = null;
		HashMap<String, Object> headers = new HashMap<String, Object>();
		HashMap<String, Object> payload = new HashMap<String, Object>();
		GregorianCalendar issue = new GregorianCalendar();
		JWT jwt = new JWT();
		issue.add(Calendar.HOUR, expTime);
		headers.put("exp", issue);
		payload.put("login", nmLogin);
		token = jwt.generate(headers, payload);
		return token;
	}
}
