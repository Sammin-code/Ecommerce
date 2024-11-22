package com.sprbt01.ecommerce.exception;


/**
 * Class Name: ResourceNotFoundException
 * Package: com.sprbt01.ecommerce.model
 * Description:此異常類用於處理資源未找到的情況，例如當查詢的數據在資料庫中不存在時拋出此異常。此類繼承自 RuntimeException，使得應用程序能夠在運行時回應此類錯誤。
 * author:
 * Create: 2024/9/9
 * Version: 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}