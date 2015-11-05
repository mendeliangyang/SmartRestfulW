/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.prepare;

import com.smart.common.model.ResponseResultCode;
import java.io.IOException;
import net.sf.json.JSONObject;

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
    private JSONObject formationResult(ResponseResultCode resultCode, String errMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("resultCode", resultCode.toString());
        jsonObject.accumulate("errMsg", errMsg);
        return jsonObject;
    }

    /**
     *
     * @param resultCode
     * @param errMsg
     * @return
     */
    private JSONObject formationResult(ResponseResultCode resultCode, Exception exception) {
        JSONObject node = formationResult(resultCode, exception.getLocalizedMessage());
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
    private JSONObject formationResult(ResponseResultCode resultCode, String errMsg, JSONObject objectNode) {
        JSONObject node = formationResult(resultCode, errMsg);
        node.accumulate("result", objectNode);
        return node;
    }

    /**
     *
     * @param resultCode
     * @param errMsg
     * @param objectNode
     * @return
     */
    public String formationResultToString(ResponseResultCode resultCode, String errMsg, JSONObject objectNode) {
        return formationResult(resultCode, errMsg, objectNode).toString();
    }

    /**
     *
     * @param objectNode
     * @return
     */
    public String formationSuccessResultToString(JSONObject objectNode) {
        return formationResult(ResponseResultCode.Success, "", objectNode).toString();
    }
}
