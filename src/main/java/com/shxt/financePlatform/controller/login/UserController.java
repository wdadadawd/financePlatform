package com.shxt.financePlatform.controller.login;

import com.alibaba.fastjson.JSON;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.ClientInfo;
import com.shxt.financePlatform.entity.LoginUser;
import com.shxt.financePlatform.entity.TeacherInfo;
import com.shxt.financePlatform.entity.User;
import com.shxt.financePlatform.service.ClientInfoService;
import com.shxt.financePlatform.service.EmailService;
import com.shxt.financePlatform.service.TeacherInfoService;
import com.shxt.financePlatform.service.UserService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import com.shxt.financePlatform.utils.JwtUtil;
import com.shxt.financePlatform.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zt
 * @create 2023-09-10 20:38
 */
@RestController
public class UserController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Resource
    private EmailService emailService;

    @Resource
    private UserService userService;

    @Resource
    private ClientInfoService clientInfoService;

    @Resource
    private TeacherInfoService teacherInfoService;


    /**
     * 登录
     * @param user 用户信息(userAccount、password)
     * @return
     */
    @PostMapping("/login")
    public R<HashMap<String, Object>> login(@RequestBody User user) {
        if (userService.getUserByAccount(user.getUserAccount()) == null)
            return R.err("账号不存在");
        //1.将用户名和密码封装
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserAccount(),user.getPassword());
        //2.根据数据库进行认证
        // 调用LoginServiceImpl中的loadUserByUsername获取到数据库中的信息后与封装数据进行比对
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            return R.err("密码错误");
        }
        //4.登录成功,生成jwt
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        String userId = loginUser.getUserId().toString();
        String jwt = JwtUtil.createJWT(userId, JSON.toJSONString(loginUser),null);   //存入id,登录信息,默认过期时间

        //5.存入redis(为用户退出的时候做处理,退出判断为jwt失效)
        redisCache.setCacheObject("login:" + userId,jwt,8, TimeUnit.HOURS);
        String identity = loginUser.getUser().getIdentity();
        //将token保存到map中返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", jwt);
        map.put("identity",identity);
        //获取个人信息
        if ("client".equals(identity))
            map.put("userInfo",clientInfoService.getById(userId));
        else if ("teacher".equals(identity))
            map.put("userInfo",teacherInfoService.getById(userId));
        return R.success(map,"登录成功");
    }


    /**
     * 退出登录
     * @return  退出结果
     */
    @RequestMapping("/user/logout")
    public R<String> logout(){
        //获取用户id
        Integer userId = AuthenticationUtils.getUserId();
        redisCache.deleteObject("login:" + userId);       //在redis中删除
        return R.success(null,"退出成功");
    }


    /**
     * 注册
     * @param user 用户信息(userAccount,password,email,userName,identity)
     * @return
     */
    @PostMapping("/register")
    public R<String> register(@RequestBody User user){
        if (redisCache.getCacheObject(user.getUserAccount()) == null){
            return R.err("太久时间未选择,注册失败");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//        System.out.println(user);
        //创建个人信息
        String identity = user.getIdentity();
        if ("teacher".equals(identity)){
            user.setProfilePhoto("https://zt12.oss-cn-beijing.aliyuncs.com/avatar/photo/%E6%95%99%E5%B8%88%E5%A4%B4%E5%83%8F.jpg");
        }else{
            user.setProfilePhoto("https://zt12.oss-cn-beijing.aliyuncs.com/avatar/photo/%E5%AD%A6%E5%91%98%E5%A4%B4%E5%83%8F.jpg");
        }
        userService.save(user);
        if ("client".equals(identity)) {
            clientInfoService.save(new ClientInfo(user.getUserId(), true, user.getUserName(),
                    "https://zt12.oss-cn-beijing.aliyuncs.com/avatar/photo/%E5%AD%A6%E5%91%98%E5%A4%B4%E5%83%8F.jpg"));
        } else if ("teacher".equals(identity)) {
            teacherInfoService.save(new TeacherInfo(user.getUserId(), "未认证", user.getUserName(),
                    "https://zt12.oss-cn-beijing.aliyuncs.com/avatar/photo/%E6%95%99%E5%B8%88%E5%A4%B4%E5%83%8F.jpg"));
        }

        return R.success(null,"注册成功");
    }


    /**
     * 刷新token
     * @return
     */
    @GetMapping("/refreshToken")
    public R<String> refreshToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);          //解析token
        System.out.println("过期时间" + claims.getExpiration());
        System.out.println("签发时间" + claims.getIssuedAt());
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId().toString();
//        System.out.println(redisCache.getCacheExpire("login:" + userId));
        //判断token有效时间，如果大于7小时
        if (redisCache.getCacheExpire("login:" + userId) > 60 * 60 * 7L)
            return R.success(token,null);
        String jwt = JwtUtil.createJWT(userId, JSON.toJSONString(loginUser),null);
        redisCache.setCacheObject("login:" + userId,jwt,8, TimeUnit.HOURS);
        return R.success(jwt,null);
    }


    /**
     * 发送注册验证码
     * @param email 邮箱
     * @return
     */
    @RequestMapping("/sendRegisterCode")
    public R<String> sendRegisterCode(@RequestParam String email){
        try {
            return emailService.sendEmail(email, "register", "注册账号");
        } catch (MessagingException e) {
            return R.err("发送失败");
        } catch (MailSendException e) {
            return R.err("邮箱错误");
        }
    }


    /**
     * 检验注册信息
     * @param user 用户信息(userAccount,password,email,userName)
     * @return
     */
    @PostMapping("/verifyRegister")
    public R<String> verifyRegister(@RequestBody User user,@RequestParam String code){
        if (!userService.checkoutEmailRepetition(user.getEmail()))
            return R.err("邮箱已被注册");
        if (!userService.checkoutAccountRepetition(user.getUserAccount()))
            return R.err("账号已存在");
        String registerCode = emailService.getMailCode(user.getEmail(), "register");
        if (registerCode == null)
            return R.err("验证码未发送或已过期");
        if (!registerCode.equals(code))
            return R.err("验证码错误");
        //暂时缓存邮箱和账号
        userService.cacheAccountAndEmail(user.getUserAccount(),user.getEmail());
        return R.success(null,"正确信息");
    }
}
