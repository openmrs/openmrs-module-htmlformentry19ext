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
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.htmlformentry.HtmlFormEntryService;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class HTMLFormEntryExtensions19Activator extends BaseModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
		
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		Context.getService(HtmlFormEntryService.class).addHandler("encounterProviderAndRole", new EncounterProviderAndRoleTagHandler());
        Context.getService(HtmlFormEntryService.class)
                .addFormSubmissionActionsExtender(HtmlFormEntryExtensions19Constants.PROVIDER_AND_ROLE_FORM_SUBMISSION_ACTIONS_EXTENDER,
                        new ProviderAndRoleFormSubmissionActionsExtender());
		log.info("HTML Form Entry Extensions for OpenMRS 1.9 Module started");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		HtmlFormEntryService hfes = Context.getService(HtmlFormEntryService.class);
		if (hfes != null) {
			hfes.getHandlers().remove("encounterProviderAndRole");
            hfes.getFormSubmissionActionsExtenders().remove(HtmlFormEntryExtensions19Constants.ENSURE_ENCOUNTER_PROVIDER_EXISTS_ACTION);
        }
		log.info("HTML Form Entry Extensions for OpenMRS 1.9 Module stopped");
	}
		
}
