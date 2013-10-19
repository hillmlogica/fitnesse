package fitnesse.responders.search;

import fitnesse.components.TraversalListener;

public class DelegatingResultResponder extends ResultResponder {
    private final ResultResponderStrategy strategy;

    public DelegatingResultResponder(ResultResponderStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String getTitle() {
        return strategy.getTitle();
    }

    @Override
    public void traverse(TraversalListener<Object> observer) {
        strategy.traverse(observer);
    }
}
