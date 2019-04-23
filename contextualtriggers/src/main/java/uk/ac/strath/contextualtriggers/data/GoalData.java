package uk.ac.strath.contextualtriggers.data;

public class GoalData extends  AbstractData
{
    public int steps;

    @Override
    public boolean equals(Object o){
        if(o instanceof GoalData){
            return ((GoalData) o).steps == (this.steps) && super.equals(o);
        }
        return false;
    }
}
