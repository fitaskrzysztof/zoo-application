package eu.iwha.dao;

import eu.iwha.model.Animals;
import eu.iwha.model.AnimalsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AnimalsDAO {

    private static final String FIND_ALL_ANIMALS_QUERY = "SELECT * FROM zoo.animals";
    private static final String INSERT_INTO_ANIMALS_QUERY = "INSERT INTO zoo.animals VALUES(?, ?, ?, ?, ?)";
    public static final String DELETE_FROM_ANIMALS_BY_ID_QUERY = "DELETE FROM zoo.animals WHERE id=?";
    public static final String SELECT_MAX_ID_QUERY = "SELECT MAX(id) FROM zoo.animals";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AnimalsDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Animals> findAll() {
        return jdbcTemplate.query(FIND_ALL_ANIMALS_QUERY, new AnimalsMapper());
    }

    public void addAnimal(Animals animal) {
        jdbcTemplate.update(INSERT_INTO_ANIMALS_QUERY, animal.getId(), animal.getName(),
                animal.getSpecie(), animal.getAge(), animal.getCage());
    }

    public void deleteAnimalById(int id){
        jdbcTemplate.update(DELETE_FROM_ANIMALS_BY_ID_QUERY, id);
    }

    public int getAutoIncrementedId(){
        return jdbcTemplate.queryForObject(SELECT_MAX_ID_QUERY, Integer.class);
    }
}
