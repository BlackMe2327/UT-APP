package run.ut.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import run.ut.app.model.domain.DataArea;
import java.util.List;

/**
 * <p>
 * 行政区域数据表 Mapper 接口
 * </p>
 *
 * @author chenwenjie
 * @since 2019-10-30
 */
public interface DataAreaMapper extends BaseMapper<DataArea> {
    @Select("select distinct(parent_id) from data_area;")
    List<Integer> selectParentIdDistinct();
}
