package fitnesse.responders.search;

import fitnesse.components.TraversalListener;

public class DelegatingResultResponder extends ResultResponder {
    private final ResultResponder resultResponder;

    public DelegatingResultResponder(ResultResponder resultResponder) {
        this.resultResponder = resultResponder;
    }

    @Override
    public String getTitle() {
        return resultResponder.getTitle();
    }

    @Override
    public void traverse(TraversalListener<Object> observer) {
        resultResponder.traverse(observer);
    }
}
