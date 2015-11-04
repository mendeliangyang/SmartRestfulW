/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.paramAnalyze;

import com.smart.common.model.OperateTypeEnum;
import com.smart.common.model.ReviveRSParamModel;
import com.smart.common.model.SignModel;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Administrator
 */
public class AnalyzeReviceParamByJackson implements IAnalyzeReviceParamModel {

    @Override
    public ReviveRSParamModel transferReviveRSParamModel(String param, OperateTypeEnum operateType) throws Exception {
        ReviveRSParamModel paramModel = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        paramModel = objectMapper.readValue(param, ReviveRSParamModel.class);
        return paramModel;
    }

    @Override
    public SignModel transferReviveRSSignModel(String param, OperateTypeEnum operateType) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
