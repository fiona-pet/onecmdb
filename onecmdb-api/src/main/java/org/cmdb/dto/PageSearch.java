package org.cmdb.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/4/19
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel("分页信息")
@Setter
@Getter
@ToString
public class PageSearch extends ListFilter {

    @ApiModelProperty("每页记录数")
    private int pageSize = 20;

    @ApiModelProperty("页号 从 1 开始")
    private int pageNumber = 1;
}
