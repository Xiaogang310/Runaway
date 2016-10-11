package com.cyou.runaway.inhouse.Command;

import org.json.JSONObject;

/**
 * Created by Gang on 2016/9/21.
 */
abstract public class CommandBase
{
    abstract public JSONObject execute(String args);
}
