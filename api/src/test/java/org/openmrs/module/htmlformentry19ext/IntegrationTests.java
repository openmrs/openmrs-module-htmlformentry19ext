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

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.openmrs.module.htmlformentry.RegressionTestHelper;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.mock.web.MockHttpServletRequest;


/**
 * Integration test for the <encounterProviderAndRole/> tag 
 */
public class IntegrationTests extends BaseModuleContextSensitiveTest {
	
	@Test
	public void testPlainTag() {
		/* Uncomment this out once the HFE test jar is published to maven
		final Date date = new Date();
		new RegressionTestHelper() {
			
			@Override
			protected String getXmlDatasetPath() {
				return "org/openmrs/module/htmlformentry19ext/include/";
			}
			
			@Override
            public String getFormName() {
				return "plainEncounterProviderAndRoleTag";
			}
			
			@Override
			public String[] widgetLabels() {
				return new String[] { "Date:", "Location:", "ProviderAndRole:" };
			}
			
			@Override
			public void setupRequest(MockHttpServletRequest request, Map<String, String> widgets) {
				request.addParameter(widgets.get("Date:"), dateAsString(date));
				request.addParameter(widgets.get("Location:"), "2");
				request.addParameter(widgets.get("ProviderAndRole:"), "1");
				request.addParameter(widgets.get("ProviderAndRole!!1:"), "1");
			}
		}.run();
		*/
	}
	
}
