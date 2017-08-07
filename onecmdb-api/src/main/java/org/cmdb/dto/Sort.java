package org.cmdb.dto;

import java.io.Serializable;

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
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel("排序")
@Setter
@Getter
@ToString
public class Sort implements Serializable {

    @ApiModelProperty("属性名称")
    public String fieldName;

    @ApiModelProperty("排序:升序 asc 降序 desc")
    public String direction;
}
