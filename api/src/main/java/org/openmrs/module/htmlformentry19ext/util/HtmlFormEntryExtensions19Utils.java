package org.openmrs.module.htmlformentry19ext.util;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HtmlFormEntryExtensions19Utils {

    /**
     * Attempts to parse the passed string as an Integer and fetch the provider role with that id
     * If no match, or the string is unparseable as an Integer, try to fetch by uuid
     *
     * @param id
     * @return
     */
    public static Object getProviderRole(String id)  {

        Object providerRole = null;

        if (StringUtils.isNotBlank(id)) {

            // see if this is parseable int; if so, try looking up by id
            Integer providerRoleId = null;

            try {
                providerRoleId = Integer.parseInt(id);
                providerRole = getProviderRoleById(providerRoleId);

                if (providerRole != null) {
                    return providerRole;
                }

            }
            catch (Exception e) {
                // ignore this, move to try by uuid
            }

            // if no match by id, look up by uuid
            providerRole = getProviderRoleBUuid(id);

        }

        return providerRole;
    }


    private static Object getProviderRoleById(Integer providerRoleId) {

        // we have to fetch the provider role by reflection, since the provider management module is not a required dependency

        try {
            Class<?> providerManagementServiceClass = Context.loadClass("org.openmrs.module.providermanagement.api.ProviderManagementService");
            Object providerManagementService = Context.getService(providerManagementServiceClass);
            Method getProviderRole = providerManagementServiceClass.getMethod("getProviderRole", Integer.class);
            return getProviderRole.invoke(providerManagementService, providerRoleId);
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to get provider role by id; the Provider Management module needs to be installed if using the providerRoles attribute", e);
        }

    }

    private static Object getProviderRoleBUuid(String providerRoleUuid) {

        // we have to fetch the provider roles by reflection, since the provider management module is not a required dependency

        try {
            Class<?> providerManagementServiceClass = Context.loadClass("org.openmrs.module.providermanagement.api.ProviderManagementService");
            Object providerManagementService = Context.getService(providerManagementServiceClass);
            Method getProviderRoleByUuid = providerManagementServiceClass.getMethod("getProviderRoleByUuid", String.class);
            return getProviderRoleByUuid.invoke(providerManagementService, providerRoleUuid);
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to get provider role by uuid; the Provider Management module needs to be installed if using the providerRoles attribute", e);
        }

    }


    public static List<Provider> getProviders(List<Object> providerRoles) {

        if (providerRoles == null || providerRoles.size() == 0) {
            return new ArrayList<Provider>();
        }

        // we have to fetch the roles by reflection, since the provider management module is not a required dependency

        try {
            Class<?> providerManagementServiceClass = Context.loadClass("org.openmrs.module.providermanagement.api.ProviderManagementService");
            Object providerManagementService = Context.getService(providerManagementServiceClass);
            Method getProvidersByRoles = providerManagementServiceClass.getMethod("getProvidersByRoles", List.class);
            Object listObj = getProvidersByRoles.invoke(providerManagementService, providerRoles);

            List<Provider> providerList = new ArrayList<Provider>();

            for (Object provider : ((List) listObj)) {
                providerList.add((Provider) provider);
            }

            return providerList;
        }
        catch(Exception e) {
            throw new RuntimeException("Unable to get providers by role; the Provider Management module needs to be installed if using the providerRoles attribute", e);
        }

    }

}
