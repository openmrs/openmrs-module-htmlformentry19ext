/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.htmlformentry19ext;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.ModuleUtil;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class HTMLFormEntryExtensions19Activator extends BaseModuleActivator {

	private static final String HTMLFORMENTRY_ID = "htmlformentry";

	private static final String HTMLFORMENTRY_19_EXT_ID = "htmlformentry19ext";

	private static final String LAST_VERSION_BEFORE_MERGE = "3.3.0";

	protected Log log = LogFactory.getLog(getClass());

	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	@Override
	public void willRefreshContext() {
		Module htmlformentry = ModuleFactory.getModuleById(HTMLFORMENTRY_ID);
		if(ModuleUtil.compareVersion(htmlformentry.getVersion(), LAST_VERSION_BEFORE_MERGE) > 0){
			log.error("Functionality of htmlformentry19ext module has been moved to htmlformentry module since 3.3.1. Unloading htmlformentry1.9");
			try {
				ModuleFactory.unloadModule(ModuleFactory.getModuleById(HTMLFORMENTRY_19_EXT_ID));
			} catch( APIException e ){
				/**
				 * method {@link HTMLFormEntryExtensions19Activator#stopped()} throws API exception
				 * because service HtmlFormEntryService is not loaded before context refresh.
				 * There is no need to do anything about it, because 'started' hasn't been invoked yet
				 * catching this error prevents logging stack trace.
                 */
			}
		}
	}

	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		Context.getService(HtmlFormEntryService.class).addHandler("encounterProviderAndRole", new EncounterProviderAndRoleTagHandler());
		log.info("HTML Form Entry Extensions for OpenMRS 1.9 Module started");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		HtmlFormEntryService hfes = Context.getService(HtmlFormEntryService.class);
		if (hfes != null)
			hfes.getHandlers().remove("encounterProviderAndRole");
		log.info("HTML Form Entry Extensions for OpenMRS 1.9 Module stopped");
	}
		
}
