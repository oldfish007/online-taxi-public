package com.mashibing.internal.dto;

import com.mashibing.internal.common.CommonStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @param <T>
 */
@Data
@Accessors(chain=true)
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;
/**
 * 成功响应的方法
 * @param <T>
 * @return
 */
    public static <T> ResponseResult success(){
        return new ResponseResult().setCode(CommonStatusEnum.SUCCESS.getCode()).setMessage(CommonStatusEnum.SUCCESS.getValue());
    }
    /**
     * 成功响应的方法
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult success(T data){
        return new ResponseResult().setCode(CommonStatusEnum.SUCCESS.getCode()).setMessage(CommonStatusEnum.SUCCESS.getValue()).setData(data);
    }
    /**
     * 失败：统一的失败
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult fail(T data){
        return new ResponseResult().setData(data);
    }
    /**
     * 失败：自定义失败 错误码和提示信息
     * @param code
     * @param message
     * @return
     */
    public static ResponseResult fail(int code,String message){
        return new ResponseResult().setCode(code).setMessage(message);
    }
    /**
     * 失败：自定义失败 错误码、提示信息、具体错误
     * @param code
     * @param message
     * @param data 调用的时候确定他的类型
     * @return
     */
    public static ResponseResult fail(int code,String message,String data){
        return new ResponseResult().setCode(code).setMessage(message).setData(data);
    }

}
