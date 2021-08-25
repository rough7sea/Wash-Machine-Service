package com.example.washmachine.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.example.washmachine.dao.WashActionDao;
import com.example.washmachine.entity.WashAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WashActionDaoImpl extends AbstractDao implements WashActionDao {

    private final String getAllActionSQLQuery;
    private final String getAllActionByIdSQLQuery;
    private final String getAllActionByMachineIdSQLQuery;
    private final String insertActionSQLQuery;
    private final String updateActionSQLQuery;
    private final String updateWashActionStatusSQLQuery;


    private static final BeanPropertyRowMapper<WashAction> ACTION_ROW_MAPPER = new BeanPropertyRowMapper<>(WashAction.class);

    @Autowired
    public WashActionDaoImpl(DataSource dataSource, @Qualifier("wash-action-data-sql") Properties sql) {
        super(dataSource);
        this.getAllActionSQLQuery = sql.getProperty("getAllActionSQLQuery");
        this.getAllActionByIdSQLQuery = sql.getProperty("getAllActionByIdSQLQuery");
        this.getAllActionByMachineIdSQLQuery = sql.getProperty("getAllActionByMachineIdSQLQuery");
        this.insertActionSQLQuery = sql.getProperty("insertActionSQLQuery");
        this.updateActionSQLQuery = sql.getProperty("updateActionSQLQuery");
        this.updateWashActionStatusSQLQuery = sql.getProperty("updateWashActionStatusSQLQuery");

    }

    @Override
    public List<WashAction> getAll() {
        return jdbcTemplate.query(getAllActionSQLQuery, ACTION_ROW_MAPPER);
    }

    @Override
    public WashAction getActionById(String actionId) {
        return jdbcTemplate.queryForObject(getAllActionByIdSQLQuery, ACTION_ROW_MAPPER, actionId);
    }

    @Override
    public List<WashAction> getActionsByMachineId(String machineId) {
        return jdbcTemplate.query(getAllActionByMachineIdSQLQuery, ACTION_ROW_MAPPER, machineId);
    }

    @Override
    public WashAction updateAction(WashAction action) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a_id", action.getActionId());
        map.put("m_id", action.getMachineId());
        map.put("a_mode", action.getWashMode().toString());
        map.put("a_status", action.getStatus().toString());

        return namedParameterJdbcTemplate.queryForObject(updateActionSQLQuery, map, ACTION_ROW_MAPPER);
    }

    @Override
    public WashAction insertAction(WashAction action) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a_id", action.getActionId());
        map.put("m_id", action.getMachineId());
        map.put("a_mode", action.getWashMode().toString());
        map.put("a_status", action.getStatus().toString());

        return namedParameterJdbcTemplate.queryForObject(insertActionSQLQuery, map, ACTION_ROW_MAPPER);
    }

    @Override
    public WashAction updateWashActionStatus(WashAction action) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a_id", action.getActionId());
        map.put("w_status", action.getStatus().toString());

        return namedParameterJdbcTemplate.queryForObject(updateWashActionStatusSQLQuery, map, ACTION_ROW_MAPPER);
    }
}
