package run.ut.app;

import org.springframework.beans.factory.annotation.Autowired;
import run.ut.app.exception.BadRequestException;
import run.ut.app.exception.handler.GlobalExceptionHandler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @className: com.hrp.security.filter-> SqlInjectFilter
 * @description: SqlInjectFilter
 * @author: SXQ
 * @createDate: 2021-11-05 10:35
 * @version: 1.0
 * @todo: TODO
 */
@WebFilter(urlPatterns = "/*", filterName = "sqlFilter")
public class SqlFilter implements Filter {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest request = servletRequest;
        ServletResponse response = servletResponse;
        //获得所有请求参数名
        Enumeration<String> names = request.getParameterNames();
        String sql = "";
        while (names.hasMoreElements()) {
            //得到参数名
            String name = names.nextElement().toString();
            //得到参数对应值
            String[] values = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                sql += values[i];
            }
        }
        if (true) {
          throw new BadRequestException("您发送请求中的参数中含有非法字符");
        } else {
            filterChain.doFilter(request, response);
        }
    }

    //效验
    protected static boolean sqlValidate(String str) {
        String s = str.toLowerCase();//统一转为小写
        s.replaceAll("\r|\n", "");//去掉空格
        String badStr =
                "select|update|and|or|delete|insert|truncate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|table|" +
                        "char|declare|sitename|xp_cmdshell|like|from|grant|use|group_concat|column_name|" +
                        "information_schema.columns|table_schema|union|where|order|by|" +
                        "'\\*|\\;|\\-|\\--|\\+|\\,|\\//|\\/|\\%|\\#";//过滤掉的sql关键字，特殊字符前面需要加\\进行转义
        String CHECKSQL = " ^(.+)\\sand\\s(.+)|(.+)\\sor(.+)\\s$ ";

        //使用正则表达式进行匹配
        if (s.matches(CHECKSQL) || s.matches(badStr)) {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}