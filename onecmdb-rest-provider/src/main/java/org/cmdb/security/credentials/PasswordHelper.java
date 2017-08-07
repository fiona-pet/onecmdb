package org.cmdb.security.credentials;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * Created by X on 2017/3/20.
 * 加密算法
 */
public class PasswordHelper {

    private String algorithmName = "md5";
    private int hashIterations = 2;


    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public String encryptPassword(String password) {
        String newPassword = new SimpleHash(
                algorithmName,
                password,
                ByteSource.Util.bytes("fiona"),
                hashIterations).toHex();
        return newPassword;
    }
}
