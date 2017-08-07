package org.cmdb.security;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/4/27
 * Time: 17:59
 * To change this template use File | Settings | File Templates.
 */
@Getter
public enum PermissionEnum {
    QUERY(1),
    CREATE(1 << 1),
    UPDATE(1 << 2),
    DELETE(1 << 3);

    private int value;

    PermissionEnum(int value) {
        this.value = value;
    }
}
