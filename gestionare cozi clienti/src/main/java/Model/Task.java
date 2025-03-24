package Model;

public class Task {
    private int arrivalTime;
    private int serviceTime;
    private final int  id;





    public Task(int arrivalTime, int serviceTime, int id) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.id=id;

    }
    public int getId(){ return id;}
    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return "Task{"+ " id= "+id+
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                '}';
    }
}
