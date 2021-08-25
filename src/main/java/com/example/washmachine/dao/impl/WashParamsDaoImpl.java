package com.example.washmachine.dao.impl;

import java.util.HashMap;
import java.util.Properties;
import javax.sql.DataSource;

import com.example.washmachine.dao.WashParamsDao;
import com.example.washmachine.entity.WashParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WashParamsDaoImpl extends AbstractDao implements WashParamsDao {

    private final String insertParamsSQLQuery;
    private final String getParamsBiActionIdSQLQuery;

    private static final BeanPropertyRowMapper<WashParams> WASP_PARAMS_ROW_MAPPER = new BeanPropertyRowMapper<>(WashParams.class);

    @Autowired
    protected WashParamsDaoImpl(DataSource dataSource,@Qualifier("wash-params-data-sql") Properties sql) {
        super(dataSource);
        this.insertParamsSQLQuery = sql.getProperty("insertParamsSQLQuery");
        this.getParamsBiActionIdSQLQuery = sql.getProperty("getParamsBiActionIdSQLQuery");
    }

    @Override
    public WashParams insertParams(WashParams washParams) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("action_id", washParams.getActionId());
        map.put("spin_power", washParams.getSpinPower());
        map.put("rinses_count", washParams.getRinsesCount());
        map.put("temperature", washParams.getTemperature());
        map.put("powder", washParams.getPowder());
        map.put("conditioner", washParams.getConditioner());

        return namedParameterJdbcTemplate.queryForObject(insertParamsSQLQuery, map, WASP_PARAMS_ROW_MAPPER);
    }

    @Override
    public WashParams getWashParams(String actionId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("a_id", actionId);
        try {
            return namedParameterJdbcTemplate.queryForObject(getParamsBiActionIdSQLQuery, map, WASP_PARAMS_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
