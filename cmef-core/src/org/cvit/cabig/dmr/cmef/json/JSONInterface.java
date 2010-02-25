package org.cvit.cabig.dmr.cmef.json;

import org.json.JSONObject;

public interface JSONInterface {
	public JSONObject toJSON(Object object);
	public Object fromJSON(JSONObject jsonText);
}
