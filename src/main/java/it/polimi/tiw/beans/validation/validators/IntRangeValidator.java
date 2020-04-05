package it.polimi.tiw.beans.validation.validators;

import it.polimi.tiw.beans.validation.annotations.IntRange;

import java.lang.annotation.Annotation;

public class IntRangeValidator implements Validator
{

    @Override
    public boolean validate(Annotation annotation, Object obj)
    {
        if(!(obj instanceof Integer))return false;
        int value = (Integer) obj;
        int max = ((IntRange)annotation).max();
        int min = ((IntRange)annotation).min();
        return value <= max && value >= min;
    }
}
