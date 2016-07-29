package com.service.impl;

import com.dao.LogDao;
import com.model.OdpsLog;
import com.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by llc on 16/7/29.
 */
@Service("LogService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogDao logDao;

    public List<OdpsLog> queryLog(String member_id, String tableName) {
        return logDao.queryLog(member_id, tableName);
    }
}
