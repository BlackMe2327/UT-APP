package run.ut.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import run.ut.app.config.redis.RedisConfig;
import run.ut.app.exception.NotFoundException;
import run.ut.app.mapper.DataAreaMapper;
import run.ut.app.model.domain.DataArea;
import run.ut.app.service.DataAreaService;
import run.ut.app.service.RedisService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataAreaServiceImpl extends ServiceImpl<DataAreaMapper, DataArea> implements DataAreaService {

    private final DataAreaMapper dataAreaMapper;
    private final RedisService redisService;

    @Override
    public List<DataArea> getAreaDataByParentId(Integer parentId) {
        String key = RedisConfig.AREA_PREFIX + "::" + parentId;
        String redisRes = redisService.get(key);
        if (StringUtils.isEmpty(redisRes)){
            List<DataArea> dataAreas = dataAreaMapper.
                    selectList(
                            new QueryWrapper<DataArea>().select("id", "parent_id", "name")
                                    .eq("parent_id", parentId).orderByAsc("sort"));
            if (!ObjectUtils.isEmpty(dataAreas)){
                String value = JSON.toJSONString(dataAreas);
                redisService.setKeyValTTL(key, value, RedisConfig.AREA_TTL);
                return dataAreas;
            }
            throw new NotFoundException("The areaa with id " + parentId + " could not be found or have been deleted");
        }
        return JSON.parseArray(redisRes, DataArea.class);
    }

    @Override
    public List<Integer> selectParentIdDistinct() {
        return dataAreaMapper.selectParentIdDistinct();
    }

    @Override
    public DataArea getById(Integer id) {
        String key = RedisConfig.AREA_INFO_PREFIX + "::" + id;
        String redisRes = redisService.get(key);
        if (StringUtils.isEmpty(redisRes)){
            DataArea dataArea = dataAreaMapper.selectById(id);
            if (!ObjectUtils.isEmpty(dataArea)){
                String value = JSON.toJSONString(dataArea);
                redisService.setKeyValTTL(key, value, RedisConfig.AREA_TTL);
                return dataArea;
            }
            throw new NotFoundException("The areaa with id " + id + " could not be found or have been deleted");
        }
        return JSON.parseObject(redisRes, DataArea.class);
    }
}
