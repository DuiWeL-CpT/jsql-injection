package com.test.auth;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;

import org.junitpioneer.jupiter.RepeatFailedTest;

import com.jsql.model.InjectionModel;
import com.jsql.model.exception.JSqlException;
import com.jsql.util.StringUtil;
import com.jsql.view.terminal.SystemOutTerminal;
import com.test.vendor.mysql.ConcreteMySqlErrorTestSuite;

import spring.security.SecurityConfiguration;

public class BasicHeaderTestSuite extends ConcreteMySqlErrorTestSuite {
    
    @Override
    public void setupInjection() throws Exception {
        
        InjectionModel model = new InjectionModel();
        this.injectionModel = model;

        model.addObserver(new SystemOutTerminal());

        model.getMediatorUtils().getParameterUtil().initializeQueryString("http://localhost:8080/basic/greeting");
        model.getMediatorUtils().getParameterUtil().setListQueryString(Arrays.asList(
            new SimpleEntry<>("tenant", "mysql-error"),
            new SimpleEntry<>("name", "0'")
        ));
        model.getMediatorUtils().getParameterUtil().setListHeader(Arrays.asList(
            new SimpleEntry<>("Authorization", "Basic " + StringUtil.base64Encode(SecurityConfiguration.BASIC_USERNAME + ":" + SecurityConfiguration.BASIC_PASSWORD))
        ));
        
        model.getMediatorUtils().getConnectionUtil().setMethodInjection(model.getMediatorMethod().getQuery());
        model.getMediatorUtils().getConnectionUtil().setTypeRequest("GET");
        
        model.setIsScanning(true);
        model.beginInjection();
    }
    
    @Override
    @RepeatFailedTest(3)
    public void listDatabases() throws JSqlException {
        super.listDatabases();
    }
}
