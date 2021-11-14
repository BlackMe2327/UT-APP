package run.ut.app;

import run.ut.app.exception.BadRequestException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "sqlFilter")
public class TestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        ServletResponse response = servletResponse;

        String token = request.getHeader("token");

        if (token == null || !token.equals("123")) {
          throw new BadRequestException("非法请求！");
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}