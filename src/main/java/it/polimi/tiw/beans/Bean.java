package it.polimi.tiw.beans;

import it.polimi.tiw.beans.validation.BeanValidator;
import it.polimi.tiw.beans.validation.Validation;

import java.util.Optional;

public class Bean
{
    private Validation validation;

    public Bean()
    {

    }

    public boolean isValid()
    {
        validation = BeanValidator.validate(this);
        return validation.isValid();
    }

    public Optional<Validation> getValidation()
    {
        return Optional.ofNullable(validation);
    }
}
