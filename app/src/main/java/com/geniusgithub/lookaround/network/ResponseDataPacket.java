package com.geniusgithub.lookaround.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.geniusgithub.lookaround.model.IParseJson;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.LogFactory;

public class ResponseDataPacket implements IParseJson{
	
	private static final CommonLog log = LogFactory.createLog();
	
	public final static String KEY_RESULT = "Result";
	public final static String KEY_DATA = "Data";
	public final static String KEY_CODE = "Code";
	public final static String KEY_MSG = "Msg";
	

    public JSONObject data = new JSONObject();
    public int id = 0;
    public String msg = "";
    public String code = "";
    
	@Override
	public boolean parseJson(JSONObject jsonObject) throws JSONException {

		JSONObject resultJsonObject = jsonObject.getJSONObject(KEY_RESULT);
		code = resultJsonObject.getString(KEY_CODE);
		msg = resultJsonObject.getString(KEY_MSG);	
		data = resultJsonObject.optJSONObject(KEY_DATA);
		if (data == null){
			data = new JSONObject();
		}
		
		return true;
	}
	
	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(
				KEY_CODE + " = " + code + "\n" + 
				KEY_MSG + " = " + msg + "\n" +
				KEY_DATA + " = " + data.toString() + "\n");
		
		return stringBuffer.toString();
		
	}

}
