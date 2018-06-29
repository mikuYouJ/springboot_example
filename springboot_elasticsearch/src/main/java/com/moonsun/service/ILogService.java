package com.moonsun.service;

import java.util.Map;

/**
 * Created by 82610 on 2018/6/25.
 */
public interface ILogService {

    /**
     * 查询日志
     * @param params
     * @return
     */
    public Map<String,Object> queryLogs(Map<String,Object> params);
}
