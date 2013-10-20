// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.responders.search;

import fitnesse.components.TraversalListener;
import fitnesse.components.WhereUsedPageFinder;
import fitnesse.wiki.WikiPage;

public class WhereUsedResponder implements ResultResponderStrategy {


    private WhereUsedResponder() {
    }

    public static ResultResponder createWhereUsedResponder() {
        return new DelegatingResultResponder(new WhereUsedResponder());
    }

    @Override
    public void traverse(TraversalListener<Object> observer, WikiPage page, WikiPage root) {
        new WhereUsedPageFinder(page, observer).search(root);
    }

    public String getTitle() {
        return "Where Used Results";
    }

}
