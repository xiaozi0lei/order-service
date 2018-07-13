package com.hckk.sgl.orderservice.controller;

import com.hckk.sgl.orderservice.entity.ReturnResult;
import com.yixin.crm.security.client.entity.UpmsUser;
import com.yixin.crm.security.client.web.bind.annotation.CurrentUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Sun Guolei 2018/7/12 16:10
 */
@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${cas.shiro.server-host}")
    private String casServerHost;
    @Value("${cas.shiro.client-host}")
    private String casClientHost;

    @GetMapping("/getLoginUsername")
    public ReturnResult<String> getLoginUsername(@CurrentUser UpmsUser currentUser) {
        String username = currentUser.getUserName();
        ReturnResult<String> result = new ReturnResult<>();
        result.setData(username);
        logger.info("登录用户名为：{}", username);
        return result;
    }

    @PostMapping(value = "/logout")
    public ReturnResult logout() {

        logger.info("开始退出...");
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        URL url;
        String host;
        try {
            url = new URL(casClientHost);
            host = url.getHost();
        } catch (MalformedURLException e) {
            return ReturnResult.success(casServerHost + "/casserver/logout?service=" + casClientHost + "/auth-web/shiro-cas");
        }
        return ReturnResult.success(casServerHost + "/casserver/logout?service=http://" + host);
    }
}
