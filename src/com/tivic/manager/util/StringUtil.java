package com.tivic.manager.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtil {

	public static List convertKeyCamelToSnake(List list) throws Exception {
		List newList = new ArrayList();
		for(Object x : list) {
			Map<String, Object> hashObject = new HashMap<String, Object>();
			Map<String, Object> hashMapObject = ObjectUtils.getFieldNamesAndValues(x);
			for (Map.Entry<String, Object> entry : hashMapObject.entrySet()) {
			    if(entry.getKey().startsWith("dt")) {
			    	GregorianCalendar data = (GregorianCalendar) entry.getValue();
			    	if(data != null)
			    		hashObject.put(StringUtil.camelToSnake(entry.getKey()), new Date(data.getTimeInMillis()));
			    }
			    else
			    	hashObject.put(StringUtil.camelToSnake(entry.getKey()), entry.getValue());
			}
			newList.add(hashObject);
		}
		return newList;
	}
	
	public static String camelToSnake(String str){
        String result = "";
        char c = str.charAt(0);
        result = result + Character.toLowerCase(c);
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result
                    = result
                      + Character.toLowerCase(ch);
            }
            else {
                result = result + ch;
            }
        }
        return result.toUpperCase();
    }
	
	public static String retirarZerosAEsquerda(String str) {
		if(str != null) {
			return str.replaceFirst("^0*", "");
		}
		return null;
	}
}
