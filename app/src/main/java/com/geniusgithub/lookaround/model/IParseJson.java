package com.geniusgithub.lookaround.model;

import org.json.JSONException;
import org.json.JSONObject;

public interface IParseJson {

	public boolean parseJson(JSONObject jsonObject) throws JSONException;
}
