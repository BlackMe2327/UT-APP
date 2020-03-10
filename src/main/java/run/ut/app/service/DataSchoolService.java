package run.ut.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import run.ut.app.model.domain.DataSchool;

import java.util.List;

public interface DataSchoolService extends IService<DataSchool> {

    /**
     * 根据行政id查询对应区的学校信息列表
     */
    List<DataSchool> getListByProvinceId(Integer provinceId);

    /**
     * 获取学校行政区id (province_id)
     */
    List<Integer> selectProvincIdDistinct();

    /**
     * 根据id获取学校信息
     */
    DataSchool getById(Integer id);

    List<DataSchool> getAllSchool();

}
