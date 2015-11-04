/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smart.smartrestfulw.interceptor;

import com.smart.common.model.ReviveRSParamModel;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Administrator
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest hsr, javax.servlet.http.HttpServletResponse hsr1, Object o) throws Exception {
        //hsr.getInputStream();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        ReviveRSParamModel paramModel = mapper.readValue(hsr.getInputStream(), ReviveRSParamModel.class);
        
//        if (paramModel.token.equals("1111")) {
//            return true;
//        } else {
//            hsr1.setStatus(404);
//        }
        return true;
    }

    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest hsr, javax.servlet.http.HttpServletResponse hsr1, Object o, ModelAndView mav) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest hsr, javax.servlet.http.HttpServletResponse hsr1, Object o, Exception excptn) throws Exception {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
