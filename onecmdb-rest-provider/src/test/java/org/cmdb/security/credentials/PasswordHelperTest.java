package org.cmdb.security.credentials;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * PasswordHelper Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>���� 20, 2017</pre>
 */
public class PasswordHelperTest {


    @Test
    public void testEncryptPassword() throws Exception {
        PasswordHelper passwordHelper = new PasswordHelper();
        System.out.println(passwordHelper.encryptPassword("admin"));

        System.out.println(UUID.fromString("test"));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String str = "aaa";
        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(str.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        String newStr = new BigInteger(1, md.digest()).toString(16);
        System.out.println(newStr);

    }


}
