package org.cmdb.repository;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

/**
 * Created by tom on 2017/3/3.
 */
public class LocalNamingStrategy extends SpringPhysicalNamingStrategy {

    // /**
    //  * 设置列名前缀。
    //  */
    // private static final String _COLUMNPREFIX = "c_";
    //
    // @Override
    // public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    //     Identifier identifier = super.toPhysicalColumnName(name, jdbcEnvironment);
    //     if (!identifier.getText().startsWith("c_")) {
    //         identifier = new Identifier(_COLUMNPREFIX + identifier.getText(), true);
    //     }
    //     return identifier;
    // }
}
