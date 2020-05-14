package it.polimi.tiw.beans.validation;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvalidBeanException extends Exception
{
    private List<Validation> validations;

    public InvalidBeanException()
    {
        super();
        validations = new ArrayList<>();
    }

    public InvalidBeanException(@Nullable List<Validation> validations)
    {
        super();
        if(validations != null)this.validations = validations;
        else this.validations = new ArrayList<>();
    }

    public InvalidBeanException(@Nullable Validation validation)
    {
        super();
        this.validations = new ArrayList<>();
        if(validation != null)this.validations.add(validation);
    }

    public List<String> getInvalidFieldNames()
    {
        return validations.stream().map(Validation::getInvalidFields).flatMap(List::stream).map(Field::getName).collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "Invalid fields: [" + String.join(", ", getInvalidFieldNames())+"]";
    }
}
