package uk.ac.strath.contextualtriggers.data;

public class NotificationData extends  AbstractData
{
    public long LastNotified;

    @Override
    public boolean equals(Object o){
        if(o instanceof NotificationData){
            return ((NotificationData) o).LastNotified == (this.LastNotified) && super.equals(o);
        }
        return false;
    }
}
