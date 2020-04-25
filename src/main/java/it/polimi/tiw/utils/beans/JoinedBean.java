package it.polimi.tiw.utils.beans;

import it.polimi.tiw.beans.Bean;

public class JoinedBean<B1 extends Bean, B2 extends Bean, B3 extends Bean>
{
    private final B1 bean1;
    private final B2 bean2;
    private final B3 bean3;

    public JoinedBean (B1 bean1, B2 bean2, B3 bean3)
    {
        this.bean1 = bean1;
        this.bean2 = bean2;
        this.bean3 = bean3;
    }

    public B1 getBean1()
    {
        return bean1;
    }

    public B2 getBean2()
    {
        return bean2;
    }

    public B3 getBean3()
    {
        return bean3;
    }
}
