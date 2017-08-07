package org.cmdb.facade;

import com.alibaba.dubbo.config.annotation.Service;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jersey.listing.ApiListingResourceJSON;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by tom on 2017/5/5.
 */
@Service(protocol = "rest")
public class DubboxSwaggerServiceImpl extends ApiListingResourceJSON implements DubboxSwaggerService {

}
