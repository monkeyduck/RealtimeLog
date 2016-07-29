package com.dao;

import com.model.OdpsLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by llc on 16/7/29.
 */
public interface LogDao {
    List<OdpsLog> queryLog(@Param("member_id") String member_id,
                           @Param("tableName") String tableName);
}
