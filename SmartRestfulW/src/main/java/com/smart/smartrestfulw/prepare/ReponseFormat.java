/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.prepare;

import com.smart.common.model.ResponseResultCode;
import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 * @author Administrator
 */
public class ReponseFormat {

    /**
     *
     * @param resultCode
     * @param errMsg
     * @return
     */
    private ObjectNode formationResult(ResponseResultCode resultCode, String errMsg) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("resultCode", resultCode.toString());
        node.put("errMsg", errMsg);
        return node;
    }

    /**
     *
     * @param resultCode
     * @param errMsg
     * @return
     */
    private ObjectNode formationResult(ResponseResultCode resultCode, Exception exception) {
        ObjectNode node = formationResult(resultCode, exception.getLocalizedMessage());
        com.smart.common.RSLogger.ErrorLogInfo(null, exception);
        return node;
    }

    /**
     *
     * @param resultCode
     * @param errMsg
     * @return
     */
    public String formationResultToString(ResponseResultCode resultCode, String errMsg) {
        return formationResult(resultCode, errMsg).toString();
    }

    /**
     *
     * @param resultCode
     * @param exception
     * @return
     */
    public String formationResultToString(ResponseResultCode resultCode, Exception exception) {
        return formationResult(resultCode, exception).toString();
    }

    /**
     *
     * @param resultCode
     * @param errMsg
     * @param objectNode
     * @return
     */
    private ObjectNode formationResult(ResponseResultCode resultCode, String errMsg, ObjectNode objectNode) {
        ObjectNode node = formationResult(resultCode, errMsg);
        node.put("result", objectNode);
        return node;
    }

    /**
     *
     * @param resultCode
     * @param errMsg
     * @param objectNode
     * @return
     */
    public String formationResultToString(ResponseResultCode resultCode, String errMsg, ObjectNode objectNode) throws IOException {
        ObjectNode node = formationResult(resultCode, errMsg);
        node.put("result", objectNode);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(node);
    }
}
