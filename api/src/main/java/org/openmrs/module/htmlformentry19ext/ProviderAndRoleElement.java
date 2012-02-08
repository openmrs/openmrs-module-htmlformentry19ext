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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.EncounterRole;
import org.openmrs.Provider;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntryContext.Mode;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.htmlformentry.widget.ErrorWidget;
import org.springframework.util.StringUtils;


/**
 *
 */
public class ProviderAndRoleElement implements HtmlGeneratorElement, FormSubmissionControllerAction {

	EncounterRoleWidget roleWidget;
	ErrorWidget roleErrorWidget;
	ProviderWidget providerWidget;
	ErrorWidget providerErrorWidget;
	
	// in case EncounterRole is specified as a parameter to the tag
	EncounterRole encounterRole;
	
	/**
     * @param parameters
     */
    public ProviderAndRoleElement(FormEntryContext context, Map<String, String> parameters) {
    	if (parameters.containsKey("encounterRole")) {
    		EncounterService es = Context.getEncounterService();
    		String param = parameters.get("encounterRole");
    		try {
    			encounterRole = es.getEncounterRole(Integer.valueOf(param));
    		} catch (Exception ex) {
    			encounterRole = es.getEncounterRoleByUuid(param);
    		}
    		if (encounterRole == null)
    			throw new RuntimeException("Cannot find EncounterRole \"" + param + "\"");
    		
    	} else {
    		roleWidget = new EncounterRoleWidget();
    		roleErrorWidget = new ErrorWidget();
    		context.registerWidget(roleWidget);
    		context.registerErrorWidget(roleWidget, roleErrorWidget);
    	}
    	providerWidget = new ProviderWidget();
    	providerErrorWidget = new ErrorWidget();
    	context.registerWidget(providerWidget);
    	context.registerErrorWidget(providerWidget, providerErrorWidget);
    	
    	boolean initialProviderSet = false;
    	if (context.getExistingEncounter() != null) {
    		if (encounterRole != null) {
    			Set<Provider> byRole = context.getExistingEncounter().getProvidersByRole(encounterRole);
    			if (byRole.size() == 1) {
    				providerWidget.setInitialValue(byRole.iterator().next());
    				initialProviderSet = true;
    			} else if (byRole.size() > 1) {
    				throw new RuntimeException("HTML Form Entry does not (yet) support multiple providers with the same encounter role");
    			} 
    		} else {
    			Map<EncounterRole, Set<Provider>> byRoles = context.getExistingEncounter().getProvidersByRoles();
    			if (byRoles.size() > 0) {
        			// currently we only support a single provider in this mode
        			if (byRoles.size() > 1 || byRoles.values().iterator().next().size() > 1) {
        				throw new RuntimeException("HTML Form Entry does not (yet) support multiple providers per encounter if you don't specify an encounterRole for each of them");
        			}
        			
    				Entry<EncounterRole, Set<Provider>> roleAndProvider = byRoles.entrySet().iterator().next();
    				Provider p = roleAndProvider.getValue().iterator().next();
    				providerWidget.setInitialValue(p);
    				initialProviderSet = true;
    				roleWidget.setInitialValue(roleAndProvider.getKey());
    			}
    		}
    	}
    	
    	if (!initialProviderSet && providerWidget != null && StringUtils.hasText(parameters.get("default"))) {
    		String temp = parameters.get("provider");
    		Provider provider = null;
    		try {
    			provider = Context.getProviderService().getProvider(Integer.valueOf(temp));
    		} catch (Exception ex) {
    			provider = Context.getProviderService().getProviderbyUuid(temp);
    		}
    		if (provider != null) {
    			providerWidget.setInitialValue(provider);
    		}
    	}
    }

	/**
     * @see org.openmrs.module.htmlformentry.element.HtmlGeneratorElement#generateHtml(org.openmrs.module.htmlformentry.FormEntryContext)
     */
    @Override
    public String generateHtml(FormEntryContext context) {
    	StringBuilder ret = new StringBuilder();
    	if (roleWidget != null) {
    		ret.append(roleWidget.generateHtml(context));
    		if (context.getMode() != Mode.VIEW)
    			ret.append(roleErrorWidget.generateHtml(context));
    		ret.append(": ");
    	}
    	if (providerWidget != null) {
			ret.append(providerWidget.generateHtml(context));
			if (context.getMode() != Mode.VIEW)
				ret.append(providerErrorWidget.generateHtml(context));
		}
	    return ret.toString();
    }
    
    /**
     * @see org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction#validateSubmission(org.openmrs.module.htmlformentry.FormEntryContext, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
    	EncounterRole role = encounterRole;
    	Provider provider = null;
    	List<FormSubmissionError> ret = new ArrayList<FormSubmissionError>();
    	if (roleWidget != null) {
    		role = (EncounterRole) roleWidget.getValue(context, submission);
    		if (role == null)
        		ret.add(new FormSubmissionError(roleWidget, Context.getMessageSourceService().getMessage("htmlformentry.error.required")));
    	}
    	if (providerWidget != null) {
    		provider = (Provider) providerWidget.getValue(context, submission);
    		if (provider == null)
        		ret.add(new FormSubmissionError(providerWidget, Context.getMessageSourceService().getMessage("htmlformentry.error.required")));
    	}
    	return ret;
    }

	/**
     * @see org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction#handleSubmission(org.openmrs.module.htmlformentry.FormEntrySession, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
    	EncounterRole role = encounterRole;
    	Provider provider = null;
    	if (roleWidget != null) {
    		role = (EncounterRole) roleWidget.getValue(session.getContext(), submission);
    	}
    	if (providerWidget != null) {
    		provider = (Provider) providerWidget.getValue(session.getContext(), submission);
    	}
    	session.getSubmissionActions().getCurrentEncounter().addProvider(role, provider);
    }

}
