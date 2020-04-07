package it.polimi.tiw.beans.validation;

import it.polimi.tiw.beans.Bean;
import it.polimi.tiw.beans.validation.validators.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BeanValidator
{

    private BeanValidator(){}

    public static Validation validate(Bean bean)
    {
        Field[] fields = bean.getClass().getDeclaredFields();
        List<Field> invalidFields = new ArrayList<>();

        try
        {
            for(Field field : fields)
            {
                for (Annotation annotation : field.getAnnotations())
                {
                    Validator validator = getValidator(annotation);
                    if(!validator.validate(annotation, getFieldValue(field, bean)))invalidFields.add(field);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Validation.invalid(null);
        }

        if(invalidFields.isEmpty())return Validation.valid();
        return Validation.invalid(invalidFields);
    }

    public static List<Validation> validate(List<Bean> beans)
    {
        return beans.stream().map(BeanValidator::validate).collect(Collectors.toList());
    }

    public static List<Validation> validate(Bean... beans)
    {
        return Arrays.stream(beans).map(BeanValidator::validate).collect(Collectors.toList());
    }

    private static Validator getValidator(Annotation annotation) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        String annotationClassName = annotation.annotationType().getSimpleName();
        String validatorClassName = Validator.class.getPackage().getName()+"."+annotationClassName+"Validator";
        Class<?> validatorClass = Class.forName(validatorClassName);
        Constructor<?> constructor = validatorClass.getConstructor();
        return (Validator) constructor.newInstance();
    }

    private static Object getFieldValue(Field field, Bean bean) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        String fieldName = field.getName();
        if(!Character.isUpperCase(fieldName.charAt(0))) fieldName = Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
        Method getter = bean.getClass().getMethod("get"+fieldName);
        return getter.invoke(bean);
    }



}
