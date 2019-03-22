package uk.ac.strath.contextualtriggers.conditions;

public abstract class DataCondition<T> extends AbstractCondition {

    private T data;

    DataCondition() {
        data = null;
    }

    DataCondition(T initialData) {
        data = initialData;
    }

    public T getData() {
        return data;
    }

    public void notifyUpdate(T data) {
        this.data = data;
        getTrigger().notifyChange();
    }

}
