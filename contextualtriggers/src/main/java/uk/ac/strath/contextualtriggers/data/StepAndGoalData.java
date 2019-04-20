package uk.ac.strath.contextualtriggers.data;

public class StepAndGoalData extends  AbstractData
{
    public StepAndGoalData()
    {
        steps = new StepData();
        goal = new GoalData();
    }
    public StepData steps;
    public GoalData goal;
}
