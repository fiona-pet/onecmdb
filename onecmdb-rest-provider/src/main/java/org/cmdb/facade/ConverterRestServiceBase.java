package org.cmdb.facade;

import org.cmdb.converter.Converter;
import org.cmdb.converter.DefaultDataConverter;
import org.cmdb.dto.ListFilter;
import org.cmdb.dto.PageSearch;
import org.cmdb.dto.RestResult;
import org.cmdb.service.CURDService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * DTO转化你的基本实现
 * Created by X on 2017/4/17.
 */
public abstract class ConverterRestServiceBase<A, B> extends DefaultDataConverter<A, B> implements CURDRestService<A>, Converter<A, B> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterRestServiceBase.class);

    public abstract CURDService<B> getService();

    @Override
    public RestResult<List<A>> list(ListFilter listFilter) {
        List<B> list = this.getService().list(listFilter);
        LOGGER.debug("list:{}", list);
        List<A> dtoList = doForwardList(list);
        LOGGER.debug("dtoList:{}", dtoList);
        return RestResult.OK(dtoList);
    }

    @Override
    public RestResult<Page<A>> page(PageSearch pageSearch) {
        Page page = this.getService().page(pageSearch);
        LOGGER.debug("pageSearch:{} => {}", pageSearch, page);
        List<A> dtoList = doForwardList(page.getContent());
        page.getContent().clear();
        page.getContent().addAll(dtoList);
        LOGGER.debug("dtoPageSearch:{} => {}", pageSearch, page);
        return RestResult.OK(page);
    }

    @Override
    public RestResult<A> create(A petRace) {
        LOGGER.debug("create or update :{}", petRace);
        B entity = doBackward(petRace);
        return RestResult.OK(this.getService().createOrUpdte(entity));
    }

    @Override
    public RestResult<String> delete(String uuid) {
        LOGGER.debug("delete :{}", uuid);
        this.getService().delete(uuid);
        return RestResult.OK("删除成功!");
    }

    @Override
    public RestResult<A> detail(String uuid) {
        B entity = getService().detail(uuid);
        A dto = doForward(entity);
        return RestResult.OK(dto);
    }
}
