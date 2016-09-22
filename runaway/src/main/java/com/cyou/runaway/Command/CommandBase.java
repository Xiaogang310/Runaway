package com.cyou.runaway.Command;

/**
 * Created by Gang on 2016/9/21.
 */
abstract public class CommandBase
{
    abstract public void Regiter();

    abstract public<T> T Execute(String... args);
}
