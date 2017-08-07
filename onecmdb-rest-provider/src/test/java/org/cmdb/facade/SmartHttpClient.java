package org.cmdb.facade;

import org.cmdb.dto.RestResult;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by X on 2017/4/14.
 */
public class SmartHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartHttpClient.class);

    private static final MediaType REQUEST_HEAD = MediaType.parse("application/json; charset=utf-8");

    private static final String HOST = "http://localhost:7890/onecmdb/api";

    private OkHttpClient client = new OkHttpClient();

    private RestResult restResult;

    public String buildToken(final String name, final String password) throws IOException {
        Map<String, Object> param = Maps.newHashMap();
        param.put("name", name);
        param.put("password", password);
        Response response = this.post("/accounts/sign-in", null, JSON.toJSONString(param));
        List<String> setCookies = response.headers("Set-Cookie");
        LOGGER.debug("cookies:{}", setCookies);
        return setCookies.get(0);
    }

    /**
     * GET请求
     *
     * @param url 地址
     * @return 结果
     * @throws IOException 异常
     */
    public Response get(String url, String token) throws IOException {
        String uri = HOST + url;
        LOGGER.debug("请求地址:{}", uri);
        Request request;
        if (Strings.isNullOrEmpty(token)) {
            request = new Request.Builder().url(uri).build();
        } else {
            request = new Request.Builder().addHeader("Cookie", token).url(uri).build();
        }
        Response response = client.newCall(request).execute();
        check(response);
        return response;
    }

    /**
     * POST 请求
     *
     * @param url 地址
     * @return 结果
     * @throws IOException 异常
     */
    public Response post(String url, String token) throws IOException {
        return this.post(url, token, "{}");
    }

    /**
     * POST 请求
     *
     * @param url  地址
     * @param json 数据
     * @return 结果
     * @throws IOException 异常
     */
    public Response post(String url, String token, String json) throws IOException {
        RequestBody body = RequestBody.create(REQUEST_HEAD, json);
        String uri = HOST + url;
        LOGGER.debug("请求地址:{}", uri);
        LOGGER.debug("请求参数:{}", json);
        Request request;
        if (Strings.isNullOrEmpty(token)) {
            request = new Request.Builder().url(uri).post(body).build();
        } else {
            request = new Request.Builder().addHeader("Cookie", token).url(uri).post(body).build();
        }
        Response response = client.newCall(request).execute();
        check(response);
        return response;
    }

    /**
     * POST 请求
     *
     * @param url 地址
     * @return 结果
     * @throws IOException 异常
     */
    public Response delete(String url, String token) throws IOException {
        String uri = HOST + url;
        LOGGER.debug("请求地址:{}", uri);
        Request request;
        if (Strings.isNullOrEmpty(token)) {
            request = new Request.Builder().url(uri).delete().build();
        } else {
            request = new Request.Builder().addHeader("Cookie", token).url(uri).delete().build();
        }
        Response response = client.newCall(request).execute();
        check(response);
        return response;
    }

    private void check(Response response) throws IOException {
        if (response.isSuccessful()) {
            String result = response.body().string();
            LOGGER.debug("返回结果:{}", result);
            restResult = JSON.parseObject(result, RestResult.class);
            if (restResult.getErrorCode() != 0) {
                throw new RuntimeException("请求失败");
            }
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public RestResult getRestResult() {
        return restResult;
    }
}
