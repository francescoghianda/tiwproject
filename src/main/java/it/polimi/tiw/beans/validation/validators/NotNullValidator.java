package it.polimi.tiw.beans.validation.validators;

import java.lang.annotation.Annotation;

public class NotNullValidator implements Validator
{

    @Override
    public boolean validate(Annotation annotation, Object obj)
    {
        return obj != null;
    }
}
