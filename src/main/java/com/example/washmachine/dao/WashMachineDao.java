package com.example.washmachine.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import com.example.washmachine.entity.WashMachine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WashMachineDao extends AbstractDao{

    private final String getWashMachineQuery;
    private final String deleteWashMachineQuery;
    private final String updateWashMachineQuery;
    private final String insertWashMachineQuery;
    private final String getAllWashMachineQuery;

    private static final BeanPropertyRowMapper<WashMachine> WASH_MACHINE_ROW_MAPPER = new BeanPropertyRowMapper<>(WashMachine.class);

    public WashMachineDao(DataSource dataSource, @Qualifier("wash-machine-data-sql") Properties sql){
        super(dataSource);
        this.getWashMachineQuery = sql.getProperty("getWashMachineSQLQuery");
        this.deleteWashMachineQuery = sql.getProperty("deleteWashMachineSQLQuery");
        this.updateWashMachineQuery = sql.getProperty("updateWashMachineSQLQuery");
        this.insertWashMachineQuery = sql.getProperty("insertWashMachineSQLQuery");
        this.getAllWashMachineQuery = sql.getProperty("getAllWashMachineSQLQuery");
    }


    public List<WashMachine> getAll() {
        return jdbcTemplate.query(getAllWashMachineQuery, WASH_MACHINE_ROW_MAPPER);
    }


    public WashMachine getWashMachine(String id) {
        return jdbcTemplate.queryForObject(getWashMachineQuery, WASH_MACHINE_ROW_MAPPER, id);
    }


    public WashMachine insertWashMachine(WashMachine washMachine) {
        HashMap<String, Object> params = new HashMap<>();

        params.put("w_id", washMachine.getId());
        params.put("w_name", washMachine.getName());
        params.put("w_status", washMachine.getStatus().toString());

        return namedParameterJdbcTemplate.queryForObject(insertWashMachineQuery, params, WASH_MACHINE_ROW_MAPPER);
    }


    public WashMachine updateWashMachine(WashMachine washMachine) {
        HashMap<String, Object> params = new HashMap<>();

        params.put("w_id", washMachine.getId());
        params.put("w_name", washMachine.getName());
        params.put("w_status", washMachine.getStatus().toString());

        return namedParameterJdbcTemplate.queryForObject(updateWashMachineQuery, params, WASH_MACHINE_ROW_MAPPER);
    }


    public boolean deleteWashMachine(String id) {
        HashMap<String, Object> params = new HashMap<>();

        params.put("m_id", id);

        return namedParameterJdbcTemplate.update(deleteWashMachineQuery, params) == 1;
    }
}
