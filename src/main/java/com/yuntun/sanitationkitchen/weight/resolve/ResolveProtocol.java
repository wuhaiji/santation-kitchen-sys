package com.yuntun.sanitationkitchen.weight.resolve;

import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;

/**
 * @author wujihong
 */
public interface ResolveProtocol {

    Boolean isResolve(byte[] dataBody);

    byte[] getNewDataBody(byte[] dataBody);

    SKDataBody resolve(byte[] dataBody);

    SKDataBody resolveAll(byte[] dataBody);
}
