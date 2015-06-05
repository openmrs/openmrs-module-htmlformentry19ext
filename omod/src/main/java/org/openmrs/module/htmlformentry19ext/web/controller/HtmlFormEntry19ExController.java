package org.openmrs.module.htmlformentry19ext.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.api.ProviderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry19ext.element.ProviderStub;
import org.openmrs.module.htmlformentry19ext.util.HtmlFormEntryExtensions19Utils;
import org.openmrs.module.htmlformentry19ext.util.MatchMode;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HtmlFormEntry19ExController {
    private Log log = LogFactory.getLog(HtmlFormEntry19ExController.class);

    @RequestMapping("/module/htmlformentry19ext/providers")
    @ResponseBody
    public Object getProviders(@RequestParam(value="searchParam",required = false) String searchParam,
                               @RequestParam(value="matchMode",required=false)MatchMode matchMode)
            throws Exception {
        ProviderService ps = Context.getProviderService();

        List<Provider> providerList = null;
        List<ProviderStub> ret = null;
        if(!StringUtils.hasText(searchParam)) {
            providerList = ps.getAllProviders();
            ret = HtmlFormEntryExtensions19Utils.getProviderStubs(providerList);
        }
        else {
            providerList = ps.getProviders(searchParam, null, null, null);
            ret = HtmlFormEntryExtensions19Utils.getProviderStubs(providerList,searchParam,matchMode);
        }

        return ret;
    }
}
