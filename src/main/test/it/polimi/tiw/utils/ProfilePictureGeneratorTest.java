package it.polimi.tiw.utils;

import org.junit.Test;

import java.io.IOException;

public class ProfilePictureGeneratorTest
{

    @Test
    public void test()
    {
        try
        {
            System.out.println(ProfilePictureGenerator.generateImage('F'));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
