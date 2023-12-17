package com.shxt.financePlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shxt.financePlatform.entity.ClientInfo;

/**
* @author 30567
* @description 针对表【client_info】的数据库操作Service
* @createDate 2023-10-20 21:47:36
*/
public interface ClientInfoService extends IService<ClientInfo> {

    Integer updateClientInfo(ClientInfo clientInfo);
}
