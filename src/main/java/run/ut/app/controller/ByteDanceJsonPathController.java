package run.ut.app.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import run.ut.app.api.AreaControllerApi;
import run.ut.app.model.dto.DataAreaDTO;
import run.ut.app.model.dto.DataSchoolDTO;
import run.ut.app.model.support.BaseResponse;
import run.ut.app.service.DataAreaService;
import run.ut.app.service.DataSchoolService;
import run.ut.app.utils.BeanUtils;
import run.ut.app.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author wenjie
 */

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("bytedance")
public class ByteDanceJsonPathController extends BaseController {

    @PostMapping("json/path")
    public BaseResponse<String> jsonPath(@RequestBody Data data) {
        return BaseResponse.ok((String) JSONPath.read(JsonUtils.objectToJson(data.getBody()), data.getPath()));
    }

    @GetMapping("/random/number")
    public BaseResponse<No> randomNumber() {
        No no = new No();
        no.setNo_1(RandomUtil.randomInt(0, 2));
        no.setNo_2(RandomUtil.randomInt(0, 2));
        return BaseResponse.ok(no);
    }

}

@lombok.Data
class No implements Serializable {
    private int no_1;
    private int no_2;
}

@lombok.Data
class Data implements Serializable {
    private Object body;
    private String path;
}
