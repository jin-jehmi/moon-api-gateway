package com.longcoding.undefined.controllers.internal;

import com.longcoding.undefined.models.internal.ThirdParty;
import com.longcoding.undefined.services.internal.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by longcoding on 19. 1. 7..
 */

@Slf4j
@RestController
@RequestMapping(value = "/internal")
public class AppController {

    @Autowired
    AppService appService;

    @RequestMapping(value = "apps", method = RequestMethod.POST)
    ThirdParty createApp(@RequestBody ThirdParty thridParty) { return appService.createApp(thridParty); }

    @RequestMapping(value = "apps/{appId}", method = RequestMethod.DELETE)
    boolean deleteApp(@PathVariable String appId) {
        return appService.deleteApp(appId);
    }

    @RequestMapping(value = "apps/{appId}", method = RequestMethod.GET)
    ThirdParty getAppInfo(@PathVariable String appId) {
        return appService.getAppInfo(appId);
    }

    @RequestMapping(value = "apps/{appId}/appKey", method = RequestMethod.DELETE)
    boolean expireAppKey(@PathVariable String appId) {
        return appService.expireAppKey(appId);
    }

    @RequestMapping(value = "apps/{appId}/appKey", method = RequestMethod.PUT)
    ThirdParty refreshAppKey(@PathVariable String appId) {
        return appService.refreshAppKey(appId);
    }

}
