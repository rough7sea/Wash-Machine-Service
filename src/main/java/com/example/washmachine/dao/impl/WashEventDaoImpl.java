package com.example.washmachine.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.example.washmachine.dao.WashEventDao;
import com.example.washmachine.entity.WashEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WashEventDaoImpl extends AbstractDao implements WashEventDao {

    private final String insertWashEventSQLQuery;
    private final String getWashEventsByActionIdSQLQuery;
    private final String getCurrentActionEventsSQLQuery;


    private static final BeanPropertyRowMapper<WashEvent> WASH_EVENT_ROW_MAPPER = new BeanPropertyRowMapper<>(WashEvent.class);

    public WashEventDaoImpl(DataSource dataSource,@Qualifier("wash-event-data-sql") Properties sql) {
        super(dataSource);
        this.insertWashEventSQLQuery = sql.getProperty("insertWashEventSQLQuery");
        this.getWashEventsByActionIdSQLQuery = sql.getProperty("getWashEventsByActionIdSQLQuery");
        this.getCurrentActionEventsSQLQuery = sql.getProperty("getCurrentActionEventsSQLQuery");
    }

    @Override
    public WashEvent insertEvent(WashEvent event) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("action_id", event.getActionId());
        map.put("step", event.getStep().toString());

        return namedParameterJdbcTemplate.queryForObject(insertWashEventSQLQuery, map, WASH_EVENT_ROW_MAPPER);
    }

    @Override
    public List<WashEvent> getEventsByActionId(String actionId) {
        return jdbcTemplate.query(getWashEventsByActionIdSQLQuery, WASH_EVENT_ROW_MAPPER, actionId);
    }

    @Override
    public WashEvent currentActionEvent(String actionId) {
        try {
            return jdbcTemplate.queryForObject(getCurrentActionEventsSQLQuery, WASH_EVENT_ROW_MAPPER, actionId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
