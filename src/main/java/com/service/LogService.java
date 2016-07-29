package com.service;

import com.model.OdpsLog;

import java.util.List;

/**
 * Created by llc on 16/7/29.
 */
public interface LogService {
    List<OdpsLog> queryLog(String member_id, String tableName);
}
