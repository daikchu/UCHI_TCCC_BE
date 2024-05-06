package com.vn.osp.notarialservices.district.reposistory;

import com.vn.osp.notarialservices.common.repository.BaseRepository;
import com.vn.osp.notarialservices.district.domain.DistrictBo;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends BaseRepository<DistrictBo,Long> {
    Optional<List<DistrictBo>> getAll();
    Optional<List<DistrictBo>> findByFilter(String stringFilter);
    Optional<BigInteger> countByFilter(String stringFilter);
}
