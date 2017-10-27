package com.baiiu.common.base.rx;


import com.baiiu.common.base.ApiResponse;
import com.baiiu.common.base.rx.exception.NotResponseException;
import com.baiiu.common.net.ApiConstants;
import com.baiiu.common.util.CommonUtil;
import rx.functions.Func1;

/**
 * author: baiiu
 * date: on 16/9/23 17:54
 * description: 自定义Function
 */
public class Functions {

    public static <T> Func1<ApiResponse<T>, T> extractResponse() {
        return apiResponse -> {

            if (CommonUtil.isNotResponse(apiResponse)) {
                if (apiResponse == null) {
                    throw new NotResponseException(ApiConstants.NET_ERROR_INFO);
                } else {
                    throw new NotResponseException(apiResponse.msg);
                }
            }

            return apiResponse.data;
        };
    }



    /**
     * 只判断code,返回该response
     */
    public static <T extends ApiResponse> Func1<T, T> filterResponse() {
        return apiResponse -> {
            if(apiResponse == null){
                throw new NotResponseException(ApiConstants.NET_ERROR_INFO);
            }

            if(apiResponse.code != 0){
                throw new NotResponseException(apiResponse.msg);
            }

            return apiResponse;
        };

    }

    /**
     * 只获取正确结果，其他则抛出异常
     */
    public static <T> Func1<ApiResponse<T>, T> simpleExtractResponse() {
        return apiResponse -> {

            if (!CommonUtil.isNotResponse(apiResponse)) {
                return apiResponse.data;
            }

            throw new NotResponseException(ApiConstants.NET_ERROR_INFO);
        };
    }


}
