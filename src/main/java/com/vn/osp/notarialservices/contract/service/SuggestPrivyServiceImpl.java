package com.vn.osp.notarialservices.contract.service;

import com.vn.osp.notarialservices.contract.domain.SuggestPrivyBO;
import com.vn.osp.notarialservices.contract.dto.SuggestPrivy;
import com.vn.osp.notarialservices.contract.repository.SuggestPrivyRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Admin on 10/1/2018.
 */
@Component
public class SuggestPrivyServiceImpl implements SuggestPrivyService {
    private static final Logger LOGGER = Logger.getLogger(SuggestPrivyServiceImpl.class);
    private final SuggestPrivyRepository suggestPrivyRepository;
    private final SuggestPrivyConverter suggestPrivyConverter;

    @Autowired
    public SuggestPrivyServiceImpl(final SuggestPrivyRepository suggestPrivyRepository, final SuggestPrivyConverter suggestPrivyConverter) {
        this.suggestPrivyRepository = suggestPrivyRepository;
        this.suggestPrivyConverter = suggestPrivyConverter;
    }
    @Override
    public Optional<Boolean> addSuggestPrivy(SuggestPrivy suggestPrivy){
        SuggestPrivyBO suggestPrivyBO = suggestPrivyConverter.convert(suggestPrivy);
        return suggestPrivyRepository.addSuggestPrivy(suggestPrivyBO);
    }
    @Override
    public Optional<List<SuggestPrivy>> searchSuggestPrivy(String template,String searchKey){
        List<SuggestPrivyBO> listBO = suggestPrivyRepository.searchSuggestPrivy(template,searchKey).orElse(new ArrayList<SuggestPrivyBO>());
        ArrayList<SuggestPrivy> list = new ArrayList<SuggestPrivy>();
        if (listBO != null && listBO.size() > 0) {
            for (int i = 0; i < listBO.size(); i++) {
                list.add(Optional.ofNullable(listBO.get(i)).map(suggestPrivyConverter::convert).orElse(new SuggestPrivy()));
            }
        }
        return Optional.of(list);

    }
}
