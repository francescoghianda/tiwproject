package it.polimi.tiw.beans.validation.validators;

import it.polimi.tiw.beans.validation.annotations.Size;

import java.lang.annotation.Annotation;

public class SizeValidator implements Validator
{
    @Override
    public boolean validate(Annotation annotation, Object obj)
    {
        if(obj == null)return false;
        String str = obj.toString();
        Size size = (Size) annotation;
        return str.length() >= size.min() && str.length() <= size.max();
    }
}
