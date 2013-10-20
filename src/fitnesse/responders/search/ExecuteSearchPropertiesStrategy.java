package fitnesse.responders.search;

import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureReadOperation;
import fitnesse.components.AttributeWikiPageFinder;
import fitnesse.components.PageFinder;
import fitnesse.components.TraversalListener;
import fitnesse.http.ChunkedResponse;
import fitnesse.http.Request;
import fitnesse.wiki.PageType;
import fitnesse.wiki.WikiPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static fitnesse.responders.search.SearchFormResponder.SEARCH_ACTION_ATTRIBUTES;
import static fitnesse.responders.search.SearchFormResponder.SPECIAL_ATTRIBUTES;
import static fitnesse.wiki.PageData.*;

public class ExecuteSearchPropertiesStrategy implements ResultResponderStrategy {

    public static final String IGNORED = "Any";
    public static final String ACTION = "Action";
    public static final String SECURITY = "Security";
    public static final String SPECIAL = "Special";

    private ExecuteSearchPropertiesStrategy() {
    }

    public static DelegatingResultResponder createExecuteSearchPropertiesResponder() {
        return new DelegatingResultResponder(new ExecuteSearchPropertiesStrategy(), true);
    }

    public SecureOperation getSecureOperation() {
        return new SecureReadOperation();
    }

    public static List<PageType> getPageTypesFromInput(Request request) {
        String requestedPageTypes = (String) request.getInput(PAGE_TYPE_ATTRIBUTE);
        if (requestedPageTypes == null) {
            return null;
        }

        List<PageType> types = new ArrayList<PageType>();

        for (String type : requestedPageTypes.split(",")) {
            types.add(PageType.fromString(type));
        }
        return types;
    }

    protected String getSuitesFromInput(Request request) {
        if (!isSuitesGiven(request))
            return null;

        return (String) request.getInput(PropertySUITES);
    }

    private boolean isSuitesGiven(Request request) {
        return request.hasInput(PropertySUITES);
    }

    public static Map<String, Boolean> getAttributesFromInput(Request request) {
        Map<String, Boolean> attributes = new LinkedHashMap<String, Boolean>();

        getListboxAttributesFromRequest(request, ACTION, SEARCH_ACTION_ATTRIBUTES,
                attributes);
        getListboxAttributesFromRequest(request, SECURITY, SECURITY_ATTRIBUTES,
                attributes);

        getListboxAttributesFromRequest(request, SPECIAL, SPECIAL_ATTRIBUTES,
                attributes);

        // this is an ugly renaming we need to make
        Boolean obsoleteFlag = attributes.remove("obsolete");
        if (obsoleteFlag != null)
            attributes.put(PropertyPRUNE, obsoleteFlag);

        return attributes;
    }

    public static void getListboxAttributesFromRequest(Request request,
                                                 String inputAttributeName, String[] attributeList,
                                                 Map<String, Boolean> attributes) {
        String requested = (String) request.getInput(inputAttributeName);
        if (requested == null) {
            requested = IGNORED;
        }
        if (!IGNORED.equals(requested)) {
            for (String searchAttribute : attributeList) {
                attributes.put(searchAttribute, requested.contains(searchAttribute));
            }
        }
    }

    @Override
    public String getTitle(Request request) {
        return "Search Page Properties Results";
    }

    @Override
    public void traverse(TraversalListener<Object> observer, WikiPage page, WikiPage root, Request request, ChunkedResponse response) {
        List<PageType> pageTypes = getPageTypesFromInput(request);
        Map<String, Boolean> attributes = getAttributesFromInput(request);
        String suites = getSuitesFromInput(request);

        if (pageTypes == null && attributes.isEmpty() && suites == null) {
            response.add("No search properties were specified.");
            return;
        }

        PageFinder finder = new AttributeWikiPageFinder(observer, pageTypes,
                attributes, suites);
        finder.search(page);
    }

}
