package com.shxt.financePlatform.controller.wechat;

import com.shxt.financePlatform.common.R;
import com.shxt.financePlatform.entity.User;
import com.shxt.financePlatform.service.UserService;
import com.shxt.financePlatform.service.WxHttpRequestService;
import com.shxt.financePlatform.utils.AuthenticationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author zt
 * @create 2023-11-07 11:07
 */
@RestController
public class WeChatController {
    @Resource
    private UserService userService;

    @Resource
    private WxHttpRequestService wxHttpRequestService;

    /**
     * 用户绑定时调用的方法
     * @param openId  用户openId
     * @param userId 用户id
     * @return
     */
    @GetMapping("/binding")
    public String setOpenId(@RequestParam String openId,@RequestParam Integer userId) {
        //查找该微信是否绑定过账号
        User userByOpenId = userService.getUserByOpenId(openId);
        //查找到了绑定账号且绑定账号的id和当前要绑定的微信好一致
        if(userByOpenId != null && Objects.equals(userByOpenId.getUserId(), userId))
            return "您已经绑定过了,请不要重复绑定。";
        //否则更新用户绑定的微信号
        User user = new User(userId, openId);
        userService.updateById(user);
        if (userByOpenId == null)             //该微信号第一次绑定
            return "账号绑定成功";
        else {                                //微信改绑定
            userService.cleanOpenId(userByOpenId.getUserId());     //清除前一个号绑定的微信openId
            return "换绑成功";
        }
    }

    /**
     * 用户取消订阅时调用的方法
     * @param openId 用户openId
     */
    @GetMapping("/unsubscribe")
    public void clenOpenId(@RequestParam String openId){
        User userByOpenId = userService.getUserByOpenId(openId);      //查找数据库中wx openId对应的用户
        if (userByOpenId != null)                             //如果有绑定清除绑定
            userService.cleanOpenId(userByOpenId.getUserId());
    }


    /**
     * 获取绑定账号的微信二维码
     * @return
     */
    @GetMapping("/userTicket")
    public R<String> getUserTicket(){
        Integer userId = AuthenticationUtils.getUserId();
        String userTicketUrl = wxHttpRequestService.getUserTicket(userId);
        return R.success(userTicketUrl,null);
    }
}
