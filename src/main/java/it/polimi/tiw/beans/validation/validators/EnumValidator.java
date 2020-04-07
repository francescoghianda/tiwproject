package it.polimi.tiw.beans.validation.validators;

import it.polimi.tiw.beans.validation.annotations.Enum;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class EnumValidator implements Validator
{
    @Override
    public boolean validate(Annotation annotation, Object obj)
    {
        if(obj == null)return false;
        String[] allValues = ((Enum)(annotation)).value();
        String value = obj.toString();
        return Arrays.asList(allValues).contains(value);
    }
}
