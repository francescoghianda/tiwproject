package it.polimi.tiw.beans.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validation
{
    private final boolean valid;
    private final List<Field> invalidFields;

    private Validation(boolean valid, List<Field> invalidFields)
    {
        this.valid = valid;
        this.invalidFields = invalidFields;
    }

    public boolean isValid()
    {
        return valid;
    }

    public List<Field> getInvalidFields()
    {
        return invalidFields;
    }

    public static Validation valid()
    {
        return new Validation(true, new ArrayList<>());
    }

    public static Validation invalid(List<Field> invalidFields)
    {
        if(invalidFields == null)invalidFields = new ArrayList<>();
        return new Validation(false, invalidFields);
    }
}
