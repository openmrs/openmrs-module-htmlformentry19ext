package org.openmrs.module.htmlformentry19ext.util;

import org.apache.commons.lang.StringUtils;
import org.openmrs.PersonName;
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

    /**
     * Convenience method to get all the names of this PersonName and concatonating them together
     * with family name compoenents first, separated by a comma from given and middle names.
     * If any part of {@link #getPrefix()}, {@link #getGivenName()},
     * {@link #getMiddleName()}, etc are null, they are not included in the returned name
     *
     * @return all of the parts of this {@link PersonName} joined with spaces
     * @should not put spaces around an empty middle name
     */
    public static String getFullNameWithFamilyNameFirst(PersonName personName) {

        StringBuffer nameString = new StringBuffer();

        if (StringUtils.isNotBlank(personName.getFamilyNamePrefix())) {
            nameString.append(personName.getFamilyNamePrefix() + " ");
        }
        if (StringUtils.isNotBlank(personName.getFamilyName())) {
            nameString.append(personName.getFamilyName() + " ");
        }
        if (StringUtils.isNotBlank(personName.getFamilyName2())) {
            nameString.append(personName.getFamilyName2() + " ");
        }
        if (StringUtils.isNotBlank(personName.getFamilyNameSuffix())) {
            nameString.append(personName.getFamilyNameSuffix() + " ");
        }

        nameString.deleteCharAt(nameString.length() - 1); // delete trailing space
        nameString.append(", ");

        if (StringUtils.isNotBlank(personName.getPrefix())) {
            nameString.append(personName.getPrefix() + " ");
        }
        if (StringUtils.isNotBlank(personName.getGivenName())) {
            nameString.append(personName.getGivenName() + " ");
        }
        if (StringUtils.isNotBlank(personName.getMiddleName())) {
            nameString.append(personName.getMiddleName() + " ");
        }
        if (StringUtils.isNotBlank(personName.getDegree())) {
            nameString.append(personName.getDegree() + " ");
        }

        nameString.deleteCharAt(nameString.length() - 1); // delete trailing space
        return nameString.toString();
    }


}
