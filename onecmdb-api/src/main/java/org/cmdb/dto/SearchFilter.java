package org.cmdb.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/4/19
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel("搜索过滤条件")
@Setter
@Getter
public class SearchFilter implements Serializable {
    @ApiModelProperty("属性名称")
    public String fieldName;

    @ApiModelProperty("搜索内容")
    public Object value;

    @ApiModelProperty("搜索条件 EQ, LIKE, GT, LT, GTE, LTE")
    public SearchFilter.Operator operator;

    public SearchFilter() {
    }

    public SearchFilter(String fieldName, SearchFilter.Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public void setOperator(String operator) {
        this.operator = SearchFilter.Operator.valueOf(operator);
    }

    public static enum Operator {
        EQ,
        LIKE,
        GT,
        LT,
        GTE,
        LTE,
        NOT_EQ;

        private Operator() {
        }
    }
}
