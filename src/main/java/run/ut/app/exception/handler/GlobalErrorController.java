package run.ut.app.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import run.ut.app.exception.UtException;
import run.ut.app.model.support.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author chenwenjie.star
 * @date 2021/11/5 1:41 下午
 */
@RestController
@Slf4j
public class GlobalErrorController extends BasicErrorController {

    public GlobalErrorController() {
        this(new DefaultErrorAttributes(), new ErrorProperties());
    }

    public GlobalErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
    }

    public GlobalErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        log.error("进入error处理器");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        // 可以自定义一个handler取代下面的if else，这里为了demo足够简单先不管这么多
        if (throwable instanceof UtException) {
            UtException utException = (UtException) throwable;
            Integer statusCode = utException.getStatus().value();
            return new ResponseEntity(new BaseResponse<>(statusCode, utException.getMessage(), null), utException.getStatus());
        } else {
            return new ResponseEntity(new BaseResponse<>(500, "出现意料之外的错误", null), HttpStatus.valueOf(500));
        }
    }
}
