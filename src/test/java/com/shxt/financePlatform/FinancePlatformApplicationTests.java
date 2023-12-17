package com.shxt.financePlatform;

import com.aliyuncs.exceptions.ClientException;
import com.shxt.financePlatform.mapper.CourseMapper;
import com.shxt.financePlatform.service.OssLiveService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class FinancePlatformApplicationTests {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private OssLiveService ossLiveService;

    @Test
    void contextLoads() throws ClientException {

    }

}
