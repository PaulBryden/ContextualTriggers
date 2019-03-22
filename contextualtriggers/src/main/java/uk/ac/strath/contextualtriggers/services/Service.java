package uk.ac.strath.contextualtriggers.services;

import uk.ac.strath.contextualtriggers.conditions.DataCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Service simulates Android services, which generate data. Service is basically
 * and Observable.
 *
 * @param <T> The type of data passed to observers.
 */
abstract class Service<T> {

    private List<DataCondition<T>> conditions;

    Service() {
        conditions = new ArrayList<DataCondition<T>>();
    }

    public void addObserver(DataCondition<T> condition) {
        conditions.add(condition);
    }

    public void sendUpdate() {
        for (DataCondition<T> condition : conditions) {
            condition.notifyUpdate(getData());
        }
    }

    abstract T getData();

}
