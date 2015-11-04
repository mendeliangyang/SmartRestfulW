/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.paramAnalyze;

import java.io.IOException;
import org.codehaus.jackson.map.JsonDeserializer;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class ArrayMapDeserializer extends JsonDeserializer<Map<String, String>> {

    @Override
    public Map<String, String> deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        if (jp.getCurrentToken().equals(JsonToken.START_OBJECT)) {
            return mapper.readValue(jp, Map.class);
            //return mapper.readValue(jp, new TypeReference<HashMap<String, String>>() { });
        } else {
            //consume this stream
            mapper.readTree(jp);
            return new HashMap<String, String>();
        }
    }

}
