/**
 * Copyright 1999-2014 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cmdb.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * URL地址检查
 * 放过sigin-in,sign-ou;如果cookie不等null放过
 *
 * @author MQ
 */
@Priority(Priorities.USER)
public class TimerFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty("timer", Instant.now().toEpochMilli());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object timer = requestContext.getProperty("timer");
        if (timer != null) {
            Long beginTimer = Long.parseLong(timer.toString());
            Long endTimer = Instant.now().toEpochMilli();
            Long elapsedTime = endTimer - beginTimer;
            LOGGER.debug("{}，请求耗时: {} ms", requestContext.getUriInfo().getPath(), elapsedTime);
        }
    }
}
