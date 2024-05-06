package com.vn.osp.notarialservices.district.service;

import com.vn.osp.notarialservices.district.dto.District;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface DistrictService {
    Optional<Boolean> add(District item);
    Optional<Boolean> update(District item);
    Optional<Boolean> delete(Long id);
    Optional<List<District>> getAll();
    Optional<District> get(Long id);
    Optional<List<District>> findByFilter(String stringFilter);
    Optional<List<District>> findByCode(String stringFilter);
    Optional<Integer> countByFilter(String stringFilter);
    Optional<List<District>> listItemPage(String filter, String offset,String limit);
}
