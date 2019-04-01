package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.managers.IDataManager;

public abstract class DataCondition<T> extends AbstractCondition {

    private T data;

    DataCondition(IDataManager<T> dataManager) {
        data = null;
        dataManager.register(this);
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
