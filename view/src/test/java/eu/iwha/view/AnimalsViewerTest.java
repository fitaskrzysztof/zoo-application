package eu.iwha.view;

import eu.iwha.config.ViewConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ViewConfig.class})
public class AnimalsViewerTest {

    @Autowired
    private AnimalsViewer animalsViewer;

    @Test
    public void showAllAnimals() {
        animalsViewer.showAllAnimals();
    }

}