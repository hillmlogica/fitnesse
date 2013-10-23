// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.responders.search;

import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureReadOperation;
import fitnesse.authentication.SecureResponder;
import fitnesse.components.TraversalListener;
import fitnesse.components.Traverser;
import fitnesse.responders.ChunkingResponder;
import fitnesse.responders.templateUtilities.HtmlPage;
import fitnesse.responders.templateUtilities.PageTitle;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PathParser;

public class ResultResponder extends ChunkingResponder implements
        SecureResponder, Traverser<Object> {

    private final boolean shouldRespondWith404;
    private final ResultResponderStrategy resultResponderStrategy;

    public ResultResponder(ResultResponderStrategy resultResponderStrategy) {
        this(true, resultResponderStrategy);
    }

    public ResultResponder(boolean shouldRespondWith404, ResultResponderStrategy resultResponderStrategy) {
        this.shouldRespondWith404 = shouldRespondWith404;
        this.resultResponderStrategy = resultResponderStrategy;
    }

    protected PageCrawler getPageCrawler() {
        return root.getPageCrawler();
    }

    protected void doSending() {
        HtmlPage htmlPage = context.pageFactory.newPage();
        htmlPage.setTitle(getTitle());
        htmlPage.setPageTitle(new PageTitle(getTitle()) {
            public String getTitle() {
                return "search";
            }

            public String getLink() {
                return null;
            }
        });
        htmlPage.setMainTemplate("searchResults");

        if (page == null)
            page = context.root.getPageCrawler().getPage(context.root, PathParser.parse("FrontPage"));
        if (request.getQueryString() == null || request.getQueryString().equals(""))
            htmlPage.put("request", request.getBody());
        else
            htmlPage.put("request", request.getQueryString());
        htmlPage.put("page", page);
        htmlPage.put("viewLocation", request.getResource());
        htmlPage.setNavTemplate("viewNav");
        htmlPage.put("resultResponder", this);

        htmlPage.render(response.getWriter());

        response.closeAll();
    }

    protected String getTitle() {
        return resultResponderStrategy.getTitleForStrategy(request);
    }

    public void traverse(TraversalListener<Object> observer) {
        resultResponderStrategy.traverseForStrategy(observer, page, request, response, root);
    }

    public SecureOperation getSecureOperation() {
        return new SecureReadOperation();
    }

    protected boolean shouldRespondWith404() {
        return shouldRespondWith404;
    }
}


