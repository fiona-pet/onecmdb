package org.cmdb.service;

import org.cmdb.dto.ListFilter;
import org.cmdb.dto.PageSearch;
import org.cmdb.dto.SearchFilter;
import org.cmdb.entity.IdEntity;
import org.cmdb.entity.Idable;
import org.cmdb.repository.DaoBase;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created with IntelliJ IDEA.
 * User: mq
 * Date: 2017/4/19
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public abstract class CURDServiceBase<T extends Idable> implements CURDService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CURDServiceBase.class);

    public abstract DaoBase<T> getDao();

    public List<T> list(ListFilter listFilter) {
        LOGGER.debug("listFilter:{}", listFilter);
        Sort sort = getSort(listFilter);
        Specification spec = getSpecification(listFilter);
        List<T> list = getDao().findAll(spec, sort);
        LOGGER.debug("list:{}", list);
        return list;
    }

    public Page<T> page(PageSearch pageSearch) {
        LOGGER.debug("pageSearch:{}", pageSearch);
        Sort sort = getSort(pageSearch);
        PageRequest pageable = new PageRequest(pageSearch.getPageNumber() - 1, pageSearch.getPageSize(), sort);

        Page page;
        Specification spec = getSpecification(pageSearch);
        if (spec != null) {
            page = getDao().findAll(spec, pageable);
        } else {
            page = getDao().findAll(pageable);
        }

        LOGGER.debug("page:{}", page);
        return page;
    }

    public T detail(String uuid) {
        return this.getDao().findOne(uuid);
    }

    public T createOrUpdte(T entity) {
        if (entity instanceof IdEntity) {
            Session session = SecurityUtils.getSubject().getSession();
            Object userId = session.getAttribute("userId");
            Object businessId = session.getAttribute("businessId");
            try {
                if (null == ((IdEntity) entity).getId()) {
                    BeanUtilsBean.getInstance().setProperty(entity, "createTime", new Date());
                    BeanUtilsBean.getInstance().setProperty(entity, "createUser", userId);
                    BeanUtilsBean.getInstance().setProperty(entity, "businessId", businessId);
                    BeanUtilsBean.getInstance().setProperty(entity, "isCustom", true);
                } else {
                    Object dbEntity = detail(((IdEntity) entity).getId());
                    Map<String, Object> entityAttributeMap = PropertyUtils.describe(entity);
                    Map<String, Object> dbEntityAttributeMap = PropertyUtils.describe(dbEntity);
                    for (Map.Entry<String, Object> entry : entityAttributeMap.entrySet()) {
                        if ("id".equalsIgnoreCase(entry.getKey()) || "class".equalsIgnoreCase(entry.getKey())) {
                            continue;
                        }
                        if (null == entry.getValue() && null != dbEntityAttributeMap.get(entry.getKey())) {
                            BeanUtilsBean.getInstance().setProperty(entity, entry.getKey(), dbEntityAttributeMap.get(entry.getKey()));
                        }
                    }
                    BeanUtilsBean.getInstance().setProperty(entity, "modifyTime", new Date());
                    BeanUtilsBean.getInstance().setProperty(entity, "modifyUser", userId);
                }
            } catch (Exception var3) {
                LOGGER.warn("CU set propertie error!");
            }
        }
        return getDao().save(entity);
    }

    public void delete(String uuid) {
        Object entity = getDao().findOne(uuid);
        try {
            boolean isCustom = true;
            if (null != entity && null != PropertyUtils.describe(entity).get("isCustom")) {
                Object isCustomObj = PropertyUtils.getNestedProperty(entity, "isCustom");
                isCustom = Boolean.parseBoolean(isCustomObj.toString());
            }
            if (!isCustom) {
                throw new RuntimeException("系统默认数据不能删除");
            }
            getDao().delete(uuid);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    private Sort getSort(ListFilter listFilter) {
        Sort sort;
        if (listFilter.getSort() != null) {
            Splitter splitter = Splitter.on(",").omitEmptyStrings().trimResults();
            sort = new Sort(Sort.Direction.fromString(listFilter.getSort().getDirection()), splitter.splitToList(listFilter.getSort().getFieldName()));
        } else {
            sort = new Sort(Sort.Direction.fromString("asc"), new String[]{"id"});
        }
        return sort;
    }

    private Specification getSpecification(ListFilter listFilter) {
        Session session = SecurityUtils.getSubject().getSession();
        Object businessId = session.getAttribute("businessId");
        SearchFilter searchFilter = null;
        if (businessId != null) {
            searchFilter = new SearchFilter();
            searchFilter.setFieldName("businessId");
            searchFilter.setOperator(SearchFilter.Operator.EQ.name());
            searchFilter.setValue(businessId);
        }
        Specification spec = null;
        if (null != listFilter.getAndFilters() && !listFilter.getAndFilters().isEmpty()) {
            if (searchFilter != null) {
                listFilter.getAndFilters().add(searchFilter);
            }
            spec = this.bySearchFilter(listFilter.getFilters(), listFilter.getAndFilters());
        } else if (null != listFilter.getFilters() && !listFilter.getFilters().isEmpty()) {
            if (searchFilter != null) {
                listFilter.getFilters().add(searchFilter);
            }
            spec = this.bySearchFilter(listFilter.getFilters());
        }
        return spec;
    }

    private Specification<T> bySearchFilter(final List<SearchFilter> filters) {
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate orFilter = CURDServiceBase.this.toPredicates(root, criteriaBuilder, filters, "OR");
                return criteriaBuilder.or(new Predicate[]{orFilter});
            }
        };
        return specification;
    }

    private Specification<T> bySearchFilter(final List<SearchFilter> filters,
                                            final List<SearchFilter> andFilters) {
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate orFilter = CURDServiceBase.this.toPredicates(root, criteriaBuilder, filters, "OR");
                Predicate andFilter = CURDServiceBase.this.toPredicates(root, criteriaBuilder, andFilters, "AND");
                return criteriaBuilder.and(orFilter, andFilter);
            }
        };
        return specification;
    }

    private static Predicate toPredicates(Root root, CriteriaBuilder builder, List<SearchFilter> filters, String whereType) {
        ArrayList predicates = Lists.newArrayList();
        Iterator it = filters.iterator();

        while (it.hasNext()) {
            SearchFilter filter = (SearchFilter) it.next();
//            String[] names = StringUtils.split(filter.fieldName, ".");
            Path expression = root.get(filter.fieldName);

           /* for (int i = 1; i < names.length; ++i) {
                expression = expression.get(names[i]);
            }*/

            switch (filter.getOperator()) {
                case EQ:
                    predicates.add(builder.equal(expression, filter.value));
                    break;
                case LIKE:
                    predicates.add(builder.like(expression, "%" + filter.value + "%"));
                    break;
                case GT:
                    predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                    break;
                case LT:
                    predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                    break;
                case GTE:
                    predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                    break;
                case LTE:
                    predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                    break;
                case NOT_EQ:
                    predicates.add(builder.notEqual(expression, (Comparable) filter.value));
            }
        }

        if (!predicates.isEmpty()) {
            if ("AND".equalsIgnoreCase(whereType)) {
                return builder.and((Predicate[]) predicates.toArray(new Predicate[predicates.size()]));
            } else {
                return builder.or((Predicate[]) predicates.toArray(new Predicate[predicates.size()]));
            }
        } else {
            return builder.conjunction();
        }
    }

}
