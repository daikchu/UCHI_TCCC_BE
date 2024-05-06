package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.contract.domain.SuggestPropertyBO;
import com.vn.osp.notarialservices.contract.dto.SuggestProperty;
import com.vn.osp.notarialservices.contract.repository.SuggestPropertyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 15/1/2018.
 */
@Component
public class SuggestPropertyServiceImpl implements SuggestPropertyService {
    private static final Logger LOGGER = Logger.getLogger(SuggestPrivyServiceImpl.class);
    private final SuggestPropertyRepository suggestPropertyRepository;
    private final SuggestPropertyConverter suggestPropertyConverter;

    @Autowired
    public SuggestPropertyServiceImpl(final SuggestPropertyRepository suggestPropertyRepository, final SuggestPropertyConverter suggestPropertyConverter) {
        this.suggestPropertyRepository = suggestPropertyRepository;
        this.suggestPropertyConverter = suggestPropertyConverter;
    }
    @Override
    public Optional<Boolean> addSuggestProperty(SuggestProperty suggestProperty){
        SuggestPropertyBO suggestPropertyBO = suggestPropertyConverter.convert(suggestProperty);
        return suggestPropertyRepository.addSuggestProperty(suggestPropertyBO);
    }
    @Override
    public Optional<List<SuggestProperty>> searchSuggestProperty(String type, String searchKey){
        List<SuggestPropertyBO> listBO = suggestPropertyRepository.searchSuggestProperty(type,searchKey).orElse(new ArrayList<SuggestPropertyBO>());
        ArrayList<SuggestProperty> list = new ArrayList<SuggestProperty>();
        if (listBO != null && listBO.size() > 0) {
            for (int i = 0; i < listBO.size(); i++) {
                list.add(Optional.ofNullable(listBO.get(i)).map(suggestPropertyConverter::convert).orElse(new SuggestProperty()));
            }
        }
        return Optional.of(list);

    }
}
