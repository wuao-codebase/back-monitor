package top.watech.backmonitor.util;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;
import top.watech.backmonitor.entity.Audience;
import top.watech.backmonitor.enums.ResultEnum;
import top.watech.backmonitor.exception.LoginException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fhm on 2018/7/30.
 * JWT过滤器
 */
public class JwtFilter extends GenericFilterBean {
    @Autowired
    private Audience audience;

    /**
     * 没携带token页面过滤
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        //等到请求头信息authorization信息
        final String authHeader = request.getHeader("Authentication-Token");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
        } else {

            if (authHeader == null || !authHeader.startsWith("bearer ")) {
                throw new LoginException(ResultEnum.LOGIN_ERROR);
            }
            //token是bearer;之后的部分
            final String token = authHeader.substring(7);

            try {
                if(audience == null){
                    BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    audience = (Audience) factory.getBean("audience");
                }
                final Claims claims = JwtHelper.parseJWT(token,audience.getBase64Secret());
                if(claims == null){
                    throw new LoginException(ResultEnum.LOGIN_ERROR);
                }
                request.setAttribute(Constants.CLAIMS, claims);
            } catch (final Exception e) {
                throw new LoginException(ResultEnum.LOGIN_ERROR);
            }
            chain.doFilter(req, res);
        }
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
    }
}
