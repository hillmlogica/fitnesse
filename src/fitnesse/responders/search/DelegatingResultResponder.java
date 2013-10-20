package fitnesse.responders.search;

import fitnesse.components.TraversalListener;

public class DelegatingResultResponder extends ResultResponder {
    private final ResultResponderStrategy strategy;
    private final boolean shouldRespondWith404;

    public DelegatingResultResponder(ResultResponderStrategy strategy, boolean shouldRespondWith404) {
        this.strategy = strategy;
        this.shouldRespondWith404 = shouldRespondWith404;
    }

    @Override
    public String getTitle() {
        return strategy.getTitle(request);
    }

    @Override
    public void traverse(TraversalListener<Object> observer) {
        strategy.traverse(observer, page, root, request, response);
    }

    @Override
    protected boolean shouldRespondWith404() {
        return shouldRespondWith404;
    }

    public Class<? extends ResultResponderStrategy> strategyClass() {
        return strategy.getClass();
    }
}
