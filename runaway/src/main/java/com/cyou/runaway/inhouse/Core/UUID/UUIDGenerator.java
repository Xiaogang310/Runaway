package com.cyou.runaway.inhouse.Core.UUID;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;


/**
 * Created by Gang on 2016/10/9.
 *
 * 获取唯一标示，目前采用优先适配ANDROID_ID，如果无效随机生成UUID的策略
 * 1. 不使用 WIFI,Bluetooth的地址的原因是如果WIFI和Bluetooth关闭那么将返回null
 * 一台手机开关WIFI的状态会生产不同的UUID
 * 2. 不使用DeviceID的原因是：(摘自Android Developers blog)
 *      Non-phones: Wifi-only devices or music players that don’t have telephony hardware just don’t have this kind of unique identifier.
 *      Persistence: On devices which do have this, it persists across device data wipes and factory resets. It’s not clear at all if, in this situation, your app should regard this as the same device.
 *      Privilege:It requires READ_PHONE_STATE permission, which is irritating if you don’t otherwise use or need telephony.
 *      Bugs: We have seen a few instances of production phones for which the implementation is buggy and returns garbage, for example zeros or asterisks.
 *    特别是需要READ_PHONE_STATE的权限，用户很容易认为这是在窃取隐私给游戏差评
 */

public class UUIDGenerator
{
    protected String mUUID;
    protected Context mContext;
    private String mMaigc = "9774d56d682e549c";

    public UUIDGenerator(Context context)
    {
        mUUID = null;
        mContext = context;
    }

    public String getUUID()
    {
        generateAndroidID().generateSN().generateInstallation();

        return mUUID;
    }

    protected UUIDGenerator generateAndroidID()
    {
        if (null == mUUID)
        {
            String uuid = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            if (!mMaigc.equals(uuid))
            {
                mUUID = uuid;
            }
        }

        return this;
    }

    protected UUIDGenerator generateSN()
    {
        if (null == mUUID)
        {
            String sn = Build.SERIAL;

            if (null != sn)
                mUUID = sn;
        }

        return this;
    }

    protected UUIDGenerator generateInstallation()
    {
        if (null == mUUID)
        {
            String uuid;

            try
            {
                uuid = InstallationUUID.getUUID(mContext);
            }
            catch (Exception e)
            {
                uuid = null;
            }

            mUUID = uuid;
        }

        return this;
    }
}