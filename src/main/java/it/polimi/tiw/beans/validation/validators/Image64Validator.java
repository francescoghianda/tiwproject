package it.polimi.tiw.beans.validation.validators;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class Image64Validator implements Validator
{
    public static final Pattern pattern = Pattern.compile("data:image\\/([a-zA-Z]*);base64,([^\\\"]*)");

    @Override
    public boolean validate(Annotation annotation, Object obj)
    {
        if(obj == null || obj.toString().isEmpty())return false;
        return pattern.matcher(obj.toString()).matches();
    }
}
