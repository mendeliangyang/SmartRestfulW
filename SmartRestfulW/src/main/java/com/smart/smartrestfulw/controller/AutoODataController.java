/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.controller;

import com.smart.common.DBHelper;
import com.smart.common.DeployInfo;
import com.smart.common.PoiExcelHelper;
import com.smart.common.SignVerify.SignCommon;
import com.smart.common.SignVerify.SignInformationModel;
import com.smart.common.model.ExecuteResultParam;
import com.smart.common.model.ResponseResultCode;
import com.smart.common.UtileSmart;
import com.smart.common.model.OperateTypeEnum;
import com.smart.common.model.ReviveRSParamModel;
import com.smart.common.model.SqlFactoryResultModel;
import com.smart.smartrestfulw.prepare.ReponseFormat;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Web Service
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/autoOD")
public class AutoODataController {

    //private FormationResult formationResult = new FormationResult();
    private final ReponseFormat responseFormat = new ReponseFormat();
    private final AnalyzeReviceParamModel analyzeParamModel = new AnalyzeReviceParamModel();

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
    public String test(@RequestBody String param) throws Exception {
        //analyzeParamJackson.transferReviveRSParamModel(param, OperateTypeEnum.update);
        return "test past";
    }

    @RequestMapping(value = "/SelectModel", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String SelectModel(@RequestBody String param) {
        ExecuteResultParam resultParam = null, resultParam1 = null;
        String sqlStr = null, sqlStr1 = null;
        ReviveRSParamModel paramModel = null;
        try {
            paramModel = analyzeParamModel.transferReviveRSParamModel(param, OperateTypeEnum.select);
            SignInformationModel signModel = SignCommon.verifySign(paramModel.getToken(), false);
            if (signModel == null) {
                return responseFormat.formationResultToString(ResponseResultCode.ErrorSignToken, "no authorize");
            }

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
            if (sqlStr1 != null) {
                resultParam1 = DBHelper.ExecuteSqlSelect(paramModel.rsid, sqlStr1);
            }
            if (resultParam.ResultCode >= 0) {
                if (resultParam1 != null && resultParam1.ResultCode >= 0) {
                    JSONArray rowsCountJson = resultParam1.ResultJsonObject.getJSONArray(DeployInfo.ResultDataTag);
                    Iterator iterator = rowsCountJson.iterator();
                    JSONObject rowsCount = (JSONObject) iterator.next();
                    resultParam.ResultJsonObject.accumulate("rowsCount", rowsCount.getString("rowsCount"));
                }
                return responseFormat.formationSuccessResultToString(resultParam.ResultJsonObject);
            } else {
                return responseFormat.formationResultToString(ResponseResultCode.Error, resultParam.errMsg);
            }
        } catch (Exception e) {
            return responseFormat.formationResultToString(ResponseResultCode.Error, e.getLocalizedMessage());
        } finally {
            if (paramModel != null) {
                paramModel.destroySelf();
            }
            UtileSmart.FreeObjects(resultParam, resultParam1, paramModel, sqlStr, sqlStr1);
        }
    }

    @RequestMapping(value = "/SelectModelCount", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String SelectModelCount(@RequestBody String param) {
        ExecuteResultParam resultParam = null;
        String sqlStr = null;

        ReviveRSParamModel paramModel = null;
        try {
            paramModel = analyzeParamModel.transferReviveRSParamModel(param, OperateTypeEnum.select);

            SignInformationModel signModel = SignCommon.verifySign(paramModel.getToken(), false);
            if (signModel == null) {
                return responseFormat.formationResultToString(ResponseResultCode.ErrorSignToken, "no authorize");
            }

            sqlStr = DBHelper.SqlSelectCountFactory(paramModel);
            //执行sql查询
            resultParam = DBHelper.ExecuteSqlSelect(paramModel.rsid, sqlStr);
            if (resultParam.ResultCode >= 0) {
                return responseFormat.formationSuccessResultToString(resultParam.ResultJsonObject);
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
    public String UpdateModel(@RequestBody String param) {
        ExecuteResultParam resultParam = null;
        ReviveRSParamModel paramModel = null;
        try {
            paramModel = analyzeParamModel.transferReviveRSParamModel(param, OperateTypeEnum.update);

            SignInformationModel signModel = SignCommon.verifySign(paramModel.getToken(), false);
            if (signModel == null) {
                return responseFormat.formationResultToString(ResponseResultCode.ErrorSignToken, "no authorize");
            }
            resultParam = DBHelper.ExecuteSql(paramModel.rsid, DBHelper.SqlUpdateFactory(paramModel));

            if (resultParam.ResultCode >= 0) {
                //JMSQueueMessage.AsyncWriteMessage(paramModel.db_tableName, 2, paramModel.pkValues);
                return responseFormat.formationSuccessResultToString(resultParam.ResultJsonObject);
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
    public String DeleteModel(@RequestBody String param) {
        ExecuteResultParam resultParam = null;
        ReviveRSParamModel paramModel = null;
        try {
            paramModel = analyzeParamModel.transferReviveRSParamModel(param, OperateTypeEnum.delete);

            SignInformationModel signModel = SignCommon.verifySign(paramModel.getToken(), false);
            if (signModel == null) {
                return responseFormat.formationResultToString(ResponseResultCode.ErrorSignToken, "no authorize");
            }

            resultParam = DBHelper.ExecuteSql(paramModel.rsid, DBHelper.SqlDeleteFactory(paramModel));
            if (resultParam.ResultCode >= 0) {
                return responseFormat.formationSuccessResultToString(resultParam.ResultJsonObject);
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
    public String InsertModel(@RequestBody String param) {
        ExecuteResultParam resultParam = null;
        SqlFactoryResultModel sqlResultModel = null;
        ReviveRSParamModel paramModel = null;
        try {
            paramModel = analyzeParamModel.transferReviveRSParamModel(param, OperateTypeEnum.insert);

            SignInformationModel signModel = SignCommon.verifySign(paramModel.getToken(), false);
            if (signModel == null) {
                return responseFormat.formationResultToString(ResponseResultCode.ErrorSignToken, "no authorize");
            }

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
                return responseFormat.formationSuccessResultToString(resultParam.ResultJsonObject);
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

    @RequestMapping(value = "/ExportExcel", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String ExportExcel(String param) {
        ReviveRSParamModel paramModel = null;
        String sqlStr = null, sqlStr1 = null;

        try {
            paramModel = analyzeParamModel.transferReviveRSParamModel(param, OperateTypeEnum.exportExcel);

            SignInformationModel signModel = SignCommon.verifySign(paramModel.getToken(), false);
            if (signModel == null) {
                return responseFormat.formationResultToString(ResponseResultCode.ErrorSignToken, "no authorize");
            }

            //判断是否有分页
            if ((paramModel.db_pageNum != -1 && paramModel.db_pageSize != -1) || (paramModel.db_skipNum != -1 && paramModel.db_topNum != -1)) {
                //调用分页的sql语句构造
                sqlStr = DBHelper.SqlSelectPageFactory(paramModel);
            } else {
                sqlStr = DBHelper.SqlSelectFactory(paramModel);
            }
            //执行sql查询
            List<Map<String, String>> resultMap = DBHelper.ExecuteSqlSelectReturnMap(paramModel.rsid, sqlStr);

            if (resultMap != null) {
                String FileName = UtileSmart.getUUID() + ".xls";
                StringBuffer exportExcelFilePath = new StringBuffer().append(DeployInfo.GetDeployTempFilePath()).append(File.separator).append(FileName);

                File exportExcelFile = new File(exportExcelFilePath.toString());
                System.out.println(exportExcelFilePath);
                PoiExcelHelper.productExcelFile(resultMap, paramModel.db_exportColumns, paramModel.db_tableName, exportExcelFile);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("file", DeployInfo.GetHttpPath() + "/TempFile/" + FileName);
                //return formationResult.formationResult(ResponseResultCode.Success, new ExecuteResultParam(jsonObject));
                return responseFormat.formationSuccessResultToString(jsonObject);

            } else {
                return responseFormat.formationResultToString(ResponseResultCode.Error, "unknow error.");
            }
        } catch (Exception e) {
            return responseFormat.formationResultToString(ResponseResultCode.Error, e);
        } finally {
            if (paramModel != null) {
                paramModel.destroySelf();
            }
            UtileSmart.FreeObjects(paramModel, param, sqlStr, sqlStr1);
        }
    }
}
