package it.polimi.tiw.beans.validation.validators;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class EmailValidator implements Validator
{
    private static Pattern emailPattern = Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$");

    @Override
    public boolean validate(Annotation annotation, Object obj)
    {
        if(obj == null || obj.toString().isEmpty())return false;
        return emailPattern.matcher(obj.toString()).matches();
    }
}
