package com.shxt.financePlatform.service.impl.info;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shxt.financePlatform.entity.ClientInfo;
import com.shxt.financePlatform.mapper.ClientInfoMapper;
import com.shxt.financePlatform.service.ClientInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 30567
* @description 针对表【client_info】的数据库操作Service实现
* @createDate 2023-10-20 21:47:36
*/
@Service
public class ClientInfoServiceImpl extends ServiceImpl<ClientInfoMapper, ClientInfo>
    implements ClientInfoService {

    @Resource
    private ClientInfoMapper clientInfoMapper;

    /**
     * 更新用户信息
     * @param clientInfo
     */
    @Override
    public Integer updateClientInfo(ClientInfo clientInfo) {
        return clientInfoMapper.updateById(clientInfo);
    }
}




