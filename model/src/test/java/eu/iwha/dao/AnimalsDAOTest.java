package eu.iwha.dao;

import eu.iwha.config.DbConfig;
import eu.iwha.model.Animals;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DbConfig.class})
public class AnimalsDAOTest {

    @Autowired
    private AnimalsDAO animalsDAO;

    @Test
    public void shouldAddAndDeleteAnimal() throws Exception {

        long initialSize = animalsDAO.findAll().stream().count();

        Animals newAnimal = new Animals();
        newAnimal.setName("Paweu");
        newAnimal.setSpecie("Human");
        newAnimal.setAge(26);
        newAnimal.setCage(7);

        countAndCheck(initialSize);

        animalsDAO.addAnimal(newAnimal);

        List<Animals> getAllAnimalsForIdSake = animalsDAO.findAll();
        int newAnimalId = getAllAnimalsForIdSake.stream()
                .filter(a->a.getName().equals("Paweu"))
                .findAny()
                .get().getId();

        countAndCheck(initialSize+1);

        animalsDAO.deleteAnimalById(newAnimalId);

        countAndCheck(initialSize);
    }

    private void countAndCheck(long expectedListSize) {
        long numberOfAnimals = animalsDAO.findAll().stream().count();
        Assert.assertEquals(numberOfAnimals,  expectedListSize);
    }
}