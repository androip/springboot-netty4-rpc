package org.hch.rpc.common.exception;

/**
 * Created by chenghao on 9/21/16.
 */
public class RpcException extends RuntimeException{
    public RpcException(String msg){
        super(msg);
    }
}
