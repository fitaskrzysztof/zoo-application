package eu.iwha.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnimalsMapper implements RowMapper<Animals>{
    @Override
    public Animals mapRow(ResultSet rs, int rowNum) throws SQLException {
        Animals animals = new Animals();
        animals.setId(rs.getInt("id"));
        animals.setName(rs.getString("name"));
        animals.setSpecie(rs.getString("specie"));
        animals.setAge(rs.getInt("age"));
        animals.setCage(rs.getInt("cage"));
        return animals;
    }
}
