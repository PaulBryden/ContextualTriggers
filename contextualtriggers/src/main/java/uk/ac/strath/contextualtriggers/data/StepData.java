package uk.ac.strath.contextualtriggers.data;

public class StepData extends  AbstractData
{
    public int steps;

    @Override
    public boolean equals(Object o){
        if(o instanceof StepData){
            return ((StepData) o).steps == (this.steps) && super.equals(o);
        }
        return false;
    }
}
