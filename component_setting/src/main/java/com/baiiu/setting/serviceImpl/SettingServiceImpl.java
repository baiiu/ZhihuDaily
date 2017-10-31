package com.baiiu.setting.serviceImpl;

import android.content.Context;
import android.content.Intent;
import com.baiiu.componentservice.service.SettingService;
import com.baiiu.setting.SettingActivity;

/**
 * author: baiiu
 * date: on 17/10/31 15:56
 * description:
 */
public class SettingServiceImpl implements SettingService {

    @Override public void toSettingPage(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

}
