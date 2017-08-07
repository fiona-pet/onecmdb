package org.cmdb.facade;

import com.google.common.collect.Maps;

import com.alibaba.fastjson.JSON;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by X on 2017/4/13.
 */
public class AccountRestServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRestServiceTest.class);

    private SmartHttpClient client = new SmartHttpClient();

    @Test
    public void signIn() throws Exception {
        Map<String, Object> param = Maps.newHashMap();
        param.put("name", "admin");
        param.put("password", "admin");
        client.post("/accounts/sign-in", null, JSON.toJSONString(param));
    }

    @Test
    public void signOut() throws Exception {
        client.post("/accounts/sign-out", null);
    }

    @Test
    public void signUp() throws Exception {

    }

}
