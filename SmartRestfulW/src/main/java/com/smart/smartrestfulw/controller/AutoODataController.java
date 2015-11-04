/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.controller;

import com.smart.smartrestfulw.paramAnalyze.AnalyzeReviceParamModel;
import com.smart.smartrestfulw.paramAnalyze.IAnalyzeReviceParamModel;
import com.smart.common.DBHelper;
import com.smart.common.DeployInfo;
import com.smart.common.FormationResult;
import com.smart.common.model.ExecuteResultParam;
import com.smart.common.model.ResponseResultCode;
import com.smart.common.UtileSmart;
import com.smart.common.model.OperateTypeEnum;
import com.smart.common.model.ReviveRSParamModel;
import com.smart.common.model.SqlFactoryResultModel;
import com.smart.smartrestfulw.paramAnalyze.AnalyzeReviceParamByJackson;
import com.smart.smartrestfulw.prepare.ReponseFormat;
import java.math.BigDecimal;
import java.util.Iterator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/autoOD")
public class AutoODataController {

    private FormationResult formationResult = new FormationResult();
    private ReponseFormat responseFormat = new ReponseFormat();

    /**
     * Creates a new instance of ReviveRS
     */
    public AutoODataController() {
    }

    /**
     * 获取不带扩展名的文件名
     *
     * @param filename
     * @return
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String test(@RequestBody ReviveRSParamModel param) throws Exception {
        //analyzeParamJackson.transferReviveRSParamModel(param, OperateTypeEnum.update);
        return "test past";
    }

    @RequestMapping(value = "/SelectModel", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ObjectNode SelectModel(@RequestBody ReviveRSParamModel paramModel) {
        ExecuteResultParam resultParam = null, resultParam1 = null;
        String sqlStr = null, sqlStr1 = null;
        try {
//            boolean isSignOn = com.smart.common.VerificationSign.verificationSignOn(paramModel.token, paramModel.rsid);
//            if (!isSignOn) {
//                //return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam("请您先登录系统。", param));
//                return null;
//            }

            //判断是否有分页
            if ((paramModel.db_pageNum != -1 && paramModel.db_pageSize != -1) || (paramModel.db_skipNum != -1 && paramModel.db_topNum != -1)) {
                //调用分页的sql语句构造
                sqlStr = DBHelper.SqlSelectPageFactory(paramModel);
                sqlStr1 = DBHelper.SqlSelectCountFactory(paramModel);
            } else {
                sqlStr = DBHelper.SqlSelectFactory(paramModel);
            }
            //执行sql查询
            resultParam = DBHelper.ExecuteSqlSelect(paramModel.rsid, sqlStr);
            List<Map<String, String>> resultMap = DBHelper.ExecuteSqlSelectReturnMap(paramModel.rsid, sqlStr);
            String rowCounts = null;
            if (sqlStr1 != null) {
                resultParam1 = DBHelper.ExecuteSqlSelect(paramModel.rsid, sqlStr1);
                rowCounts = DBHelper.ExecuteSqlSelectOne(paramModel.rsid, sqlStr1);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode node = objectMapper.createObjectNode();
            String result = objectMapper.writeValueAsString(resultMap);
            ArrayNode arrayNode1 = objectMapper.readValue(result, ArrayNode.class);
            node.put(paramModel.db_tableName, arrayNode1);
            node.put("rowsCount", rowCounts);
            if (result != null) {
                //return node.toString();
                //return responseFormat.formationResultToString(ResponseResultCode.Success, "null", node);

            }
            if (resultParam.ResultCode >= 0) {
                if (resultParam1 != null && resultParam1.ResultCode >= 0) {
                    JSONArray rowsCountJson = resultParam1.ResultJsonObject.getJSONArray(DeployInfo.ResultDataTag);
                    Iterator iterator = rowsCountJson.iterator();
                    JSONObject rowsCount = (JSONObject) iterator.next();
                    resultParam.ResultJsonObject.accumulate("rowsCount", rowsCount.getString("rowsCount"));
                }
                //return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                //return responseFormat.formationResultToString(ResponseResultCode.Error, resultParam.errMsg);
            }

            return node;
        } catch (Exception e) {
            //return responseFormat.formationResultToString(ResponseResultCode.Error, e.getLocalizedMessage());
            return null;
        } finally {
//            if (paramModel != null) {
//                paramModel.destroySelf();
//            }
            UtileSmart.FreeObjects(resultParam, resultParam1, paramModel, sqlStr, sqlStr1);
        }
    }

    @RequestMapping(value = "/SelectModelCount", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String SelectModelCount(@RequestBody ReviveRSParamModel paramModel) {
        ExecuteResultParam resultParam = null;
        String sqlStr = null;
        try {

//            boolean isSignOn = com.smart.common.VerificationSign.verificationSignOn(paramModel.token, paramModel.rsid);
//            if (!isSignOn) {
//                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam("请您先登录系统。", param));
//            }
            sqlStr = DBHelper.SqlSelectCountFactory(paramModel);
            //执行sql查询
            resultParam = DBHelper.ExecuteSqlSelect(paramModel.rsid, sqlStr);
            if (resultParam.ResultCode >= 0) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return responseFormat.formationResultToString(ResponseResultCode.Error, resultParam.errMsg);
            }
        } catch (Exception e) {
            return responseFormat.formationResultToString(ResponseResultCode.Error, e);
        } finally {
            if (paramModel != null) {
                paramModel.destroySelf();
            }
            UtileSmart.FreeObjects(resultParam, sqlStr, paramModel);
        }
    }

    @RequestMapping(value = "/UpdateModel", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String UpdateModel(@RequestBody ReviveRSParamModel paramModel) {
        ExecuteResultParam resultParam = null;
        try {

//            boolean isSignOn = com.smart.common.VerificationSign.verificationSignOn(paramModel.token, paramModel.rsid);
//            if (!isSignOn) {
//                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam("请您先登录系统。", param));
//            }
            resultParam = DBHelper.ExecuteSql(paramModel.rsid, DBHelper.SqlUpdateFactory(paramModel));

            if (resultParam.ResultCode >= 0) {
                //JMSQueueMessage.AsyncWriteMessage(paramModel.db_tableName, 2, paramModel.pkValues);
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam());
            } else {
                return responseFormat.formationResultToString(ResponseResultCode.Error, resultParam.errMsg);
            }
        } catch (Exception e) {
            return responseFormat.formationResultToString(ResponseResultCode.Error, e);
        } finally {
            if (paramModel != null) {
                paramModel.destroySelf();
            }
            UtileSmart.FreeObjects(resultParam, paramModel);
        }

    }

    @RequestMapping(value = "/DeleteModel", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String DeleteModel(@RequestBody ReviveRSParamModel paramModel) {
        ExecuteResultParam resultParam = null;
        try {

//            boolean isSignOn = com.smart.common.VerificationSign.verificationSignOn(paramModel.token, paramModel.rsid);
//            if (!isSignOn) {
//                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam("请您先登录系统。", param));
//            }
            resultParam = DBHelper.ExecuteSql(paramModel.rsid, DBHelper.SqlDeleteFactory(paramModel));
            if (resultParam.ResultCode >= 0) {
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam());
            } else {
                return responseFormat.formationResultToString(ResponseResultCode.Error, resultParam.errMsg);
            }
        } catch (Exception e) {
            return responseFormat.formationResultToString(ResponseResultCode.Error, e);
        } finally {
            if (paramModel != null) {
                paramModel.destroySelf();
            }
            UtileSmart.FreeObjects(resultParam, paramModel);
        }
    }

    @RequestMapping(value = "/InsertModel", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String InsertModel(@RequestBody ReviveRSParamModel paramModel) {
        ExecuteResultParam resultParam = null;
        SqlFactoryResultModel sqlResultModel = null;
        try {

//            boolean isSignOn = com.smart.common.VerificationSign.verificationSignOn(paramModel.token, paramModel.rsid);
//            if (!isSignOn) {
//                return formationResult.formationResult(ResponseResultCode.Error, new ExecuteResultParam("请您先登录系统。", param));
//            }
            sqlResultModel = DBHelper.SqlInsertFactory(paramModel);
            //如果有identity 开始的sql语句以 SET NOCOUNT  ON 开始 执行查询方法
            if (sqlResultModel.strSql.startsWith("SET NOCOUNT ON")) {
                resultParam = DBHelper.ExecuteSqlOnceSelect(paramModel.rsid, sqlResultModel.strSql);
            } else {
                resultParam = DBHelper.ExecuteSql(paramModel.rsid, sqlResultModel.strSql);
            }

            if (resultParam.ResultCode >= 0) {
                //notify data changed
                //JMSQueueMessage.AsyncWriteMessage(paramModel.db_tableName, 1, paramModel.pkValues);
                if (sqlResultModel.columnValue != null && !sqlResultModel.columnValue.isEmpty()) {
                    JSONObject resultJson = new JSONObject();
                    for (String keySet : sqlResultModel.columnValue.keySet()) {
                        resultJson.accumulate(keySet, sqlResultModel.columnValue.get(keySet));
                    }
                    if (resultParam.ResultJsonObject == null) {
                        resultParam.ResultJsonObject = new JSONObject();
                    }
                    resultParam.ResultJsonObject.accumulate(DeployInfo.ResultDataTag, resultJson);
                }
                return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(resultParam.ResultJsonObject));
            } else {
                return responseFormat.formationResultToString(ResponseResultCode.Error, resultParam.errMsg);
            }
        } catch (Exception e) {
            return responseFormat.formationResultToString(ResponseResultCode.Error, e);
        } finally {
            if (paramModel != null) {
                paramModel.destroySelf();
            }
            UtileSmart.FreeObjects(resultParam, paramModel, sqlResultModel);
        }
    }
}
