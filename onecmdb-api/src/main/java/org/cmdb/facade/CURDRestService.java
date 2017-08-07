package org.cmdb.facade;

import org.cmdb.dto.ListFilter;
import org.cmdb.dto.RestResult;
import org.cmdb.dto.PageSearch;

import org.springframework.data.domain.Page;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Created by X on 2017/4/17.
 */
public interface CURDRestService<T> {

    @POST
    @Path("/list")
    @ApiOperation("列表")
    RestResult<List<T>> list(ListFilter listFilter);

    @POST
    @Path("/page")
    @ApiOperation("分页")
    RestResult<Page<T>> page(PageSearch pageSearch);

    @GET
    @Path("{id}")
    @ApiOperation("详细信息")
    RestResult<T> detail(@ApiParam("id") @PathParam("id") String var1);

    @POST
    @ApiOperation("添加/修改")
    RestResult<T> create(T var1);

    @DELETE
    @Path("{id}")
    @ApiOperation("删除")
    RestResult<String> delete(@ApiParam("id") @PathParam("id") String var1);
}
