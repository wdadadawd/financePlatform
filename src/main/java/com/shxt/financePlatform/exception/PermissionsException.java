package com.shxt.financePlatform.exception;


import com.shxt.financePlatform.common.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zt
 * @create 2023-02-16 20:32
 * 捕捉异常处理
 */
@ControllerAdvice
@ResponseBody
public class PermissionsException {

    @ExceptionHandler(MyException.class)     //抓取的异常信息
    public R<String> MyException(Exception ex){
        return R.err(ex.getMessage());
    }

}