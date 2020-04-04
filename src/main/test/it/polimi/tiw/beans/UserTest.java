package it.polimi.tiw.beans;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserTest
{
    private User user;

    @Before
    public void init()
    {
        user = new User();
    }

    @Test
    public void invalidUserTest()
    {
        if(user.isValid())fail();
        if(!user.getValidation().isPresent())fail("Validation not present!");
        List<String> invalidFields = user.getValidation().get().getInvalidFields().stream().map(Field::getName).collect(Collectors.toList());
        List<String> allFieldNames = Arrays.stream(User.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        allFieldNames.remove("serialVersionUID");
        boolean allFieldsPresent = invalidFields.containsAll(allFieldNames);
        assertTrue(allFieldsPresent);
    }

    @Test
    public void validUserTest()
    {
        user = new User("francesco.ghianda", "francesco.ghianda@mail.polimi.it", "qwerty", User.Role.MANAGER);
        assertTrue(user.isValid());
    }

}
