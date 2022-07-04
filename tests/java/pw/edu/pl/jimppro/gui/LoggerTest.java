package pw.edu.pl.jimppro.gui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pw.edu.pl.jimppro.mainpack.Launcher;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LoggerTest {
    static Logger logger;

    @BeforeAll
    public static void setUp() {
        Launcher launcher = new Launcher();
        logger = Logger.getInstance();
    }

    @Test
    void shouldEqualsText() {
        String expected = "Info for user";
        logger.setMessage("Info for user", 1);
        assertEquals(expected, logger.getText());
    }

    @Test
    void shouldEqualsColorType() {
        int expected1 = -1;
        int expected2 = 0;
        int expected3 = 1;
        logger.setMessage("Text for testing", -1);
        assertEquals(logger.getFlag(), expected1);

        logger.setMessage("Text for testing", 0);
        assertEquals(logger.getFlag(), expected2);

        logger.setMessage("Text for testing", 1);
        assertEquals(logger.getFlag(), expected3);
    }


}