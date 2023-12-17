package com.shxt.financePlatform.service.impl.file;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.shxt.financePlatform.config.OssProperties;
import com.shxt.financePlatform.exception.MyException;
import com.shxt.financePlatform.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author zt
 * @create 2023-09-22 22:33
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    /**
     * 文件上传阿里云
     * @param inputStream 文件输入流
     * @param module 文件存储的文件夹名
     * @param filename 原文件名
     * @return
     */
    @Override
    public String upload(InputStream inputStream, String module, String filename) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        log.info("OSSClient实例创建成功");
        try {
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)) {
                //创建bucket
                ossClient.createBucket(OssProperties.BUCKET_NAME);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
            }
            //构建日期路径：avatar/2019/02/26/文件名
//            String folder = new DateTime().toString("yyyy/MM/dd");
            //文件名：uuid.扩展名
            filename = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
            //文件根路径
            String key = module  + "/" + filename;
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(OssProperties.BUCKET_NAME, key, inputStream);
            // 创建PutObject请求。
            ossClient.putObject(putObjectRequest);
            //阿里云文件绝对路径
            String endpoint = OssProperties.ENDPOINT;
            //返回文件的访问路径
            return "https://" + OssProperties.BUCKET_NAME + "." + endpoint + "/" + key;
        } catch (OSSException oe) {
            throw new MyException("oss异常");
        } catch (ClientException ce) {
            throw new MyException("客户端异常");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 删除文件
     * @param url 文件地址
     */
    @Override
    public void remove(String url) {
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        log.info("OSSClient实例创建成功");
        try {
            String endpoint = OssProperties.ENDPOINT;
            //文件名（服务器上的文件路径）
            String host = "https://" + OssProperties.BUCKET_NAME + "." + endpoint + "/";
            String objectName = url.substring(host.length());
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(OssProperties.BUCKET_NAME, objectName);
        } catch (OSSException oe) {
            throw new MyException("oss异常");
        } catch (ClientException ce) {
            throw new MyException("客户端异常");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();          //关闭oss客户端
            }
        }
    }
}
