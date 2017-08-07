package org.cmdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/4/19
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
@NoRepositoryBean
public interface DaoBase<T> extends JpaRepository<T, String>, JpaSpecificationExecutor<T> {

    List<T> findAllBy();
}
