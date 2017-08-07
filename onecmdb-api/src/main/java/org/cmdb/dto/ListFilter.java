package org.cmdb.dto;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/4/25
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@ToString
public class ListFilter implements Serializable {

    @ApiModelProperty("搜索条件")
    private List<SearchFilter> filters;

    @ApiModelProperty("搜索条件")
    private List<SearchFilter> andFilters;

    @ApiModelProperty("排序")
    private Sort sort;

    public void addFilters(SearchFilter searchFilter) {
        if(filters == null) {
            filters = Lists.newArrayList();
        }
        filters.add(searchFilter);
    }

    public void addAndFilters(SearchFilter searchFilter) {
        if(andFilters == null) {
            andFilters = Lists.newArrayList();
        }
        andFilters.add(searchFilter);
    }
}
