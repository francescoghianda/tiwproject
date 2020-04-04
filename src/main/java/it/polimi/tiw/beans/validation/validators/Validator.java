package it.polimi.tiw.beans.validation.validators;

import java.lang.annotation.Annotation;

public interface Validator
{
    boolean validate(Annotation annotation, Object obj);
}
