package com.vn.osp.notarialservices.district.service;

import com.vn.osp.notarialservices.district.domain.DistrictBo;
import com.vn.osp.notarialservices.district.dto.District;
import org.springframework.stereotype.Service;

/**
 * Created by TienManh on 5/10/2017.
 */
@Service
public class DistrictConverter {

    public District convert(final DistrictBo source) {
        return new District (
                source.getId(),
                source.getName(),
                source.getCode(),
                source.getProvince_code(),
                source.getEntry_user_id(),
                source.getEntry_date_time(),
                source.getUpdate_user_id(),
                source.getUpdate_date_time());

    }
    public DistrictBo convert(final District source) {
        DistrictBo districtBo = new DistrictBo();
        districtBo.setId(source.getId());
        districtBo.setName(source.getName());
        districtBo.setCode(source.getCode());
        districtBo.setProvince_code(source.getProvince_code());
        districtBo.setEntry_user_id(source.getEntry_user_id());
        districtBo.setEntry_date_time(source.getEntry_date_time());
        districtBo.setUpdate_user_id(source.getUpdate_user_id());
        districtBo.setUpdate_date_time(source.getUpdate_date_time());
        return districtBo;
    }

}
