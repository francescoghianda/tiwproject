package it.polimi.tiw.beans.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvalidBeanException extends Exception
{
    private List<Validation> validations;

    public InvalidBeanException()
    {
        validations = new ArrayList<>();
    }

    public InvalidBeanException(List<Validation> validations)
    {
        super();
        this.validations = validations;
    }

    public InvalidBeanException(Validation validation)
    {
        super();
        this.validations = new ArrayList<>();
        this.validations.add(validation);
    }

    public List<String> getInvalidFieldNames()
    {
        return validations.stream().map(Validation::getInvalidFields).flatMap(List::stream).map(Field::getName).collect(Collectors.toList());
    }
}
