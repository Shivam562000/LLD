package Elevator

import java.util.List;

System Design;

public enum ElevatorState
{
    Idle,
    Moving,
    NotWorking;
}

public enum Direction
{
    Up, 
    Down,
    Idle;
}

public interface Dispatcher 
{
    public void submitRequest();
} 

//check wheather we can use interface or regular class
public class Button 
{
    public void pressButton(int floorId, Direction direction){};
    public void pressButton(int floorId, Direction direction, int destFloor){};
}

public class ElevatorButton extends Button {
    private Dispatcher internalDispatcher;
    private List<Floor> floors = new arrayList<Floor>();

    public ElevatorButton()
    {
        internalDispatcher = new InternalDispatcher();
    }
}

public class floorButton
{

}

public class Door
{
    private boolean isOpen;

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

    public boolean isOpen(int id) {
        //check door is open/close at perticular floor
        //Use vector/data structure to store state of doors
        return isOpen;
    }
}

public class Floor
{
    private int floorId;
    private Button floorButton;

    public floor()
    {

    }
}

// Request class representing a request made by a person
class Request {
    private int currentFloor;
    private int desiredFloor;
    private Direction direction;

    public Request(int currentFloor, int desiredFloor, Direction direction) {
        this.currentFloor = currentFloor;
        this.desiredFloor = desiredFloor;
        this.direction = direction;
    }

    public int getDesiredFloor() {
        return desiredFloor;
    }

    public Direction getDirection() {
        return direction;
    }
}

// ElevatorCar represent single elevator of system
public class ElevatorCar
{
    private int id;
    private Button internalButton;
    private Floor floor;
    private Door door;
    private Direction direction;
    private int currentFloor;

    public ElevatorCar(int id)
    {
        this.id = id;
        internalButton = new ElevatorButton();
        floor = new Floor();
        door = new Door();
        direction = Direction.Idle;
        currentFloor = 0;
    }

    // it will send request using elevator Controller Strategy (algo) and call move method
    public void pressButton(int destFloor)
    {
        Direction direction = Direction.Idle;
        if(destFloor > currentFloor)
            direction = Direction.UP;
        else if(destFloor < currentFloor)
            direction = Direction.DOWN;
        button.pressButton(destFloor, direction, id);
    }

    // using elevator Controller Strategy, it will move elevator
    public void move(int floorId, Direction direction)
    {
        //here continues check about new requests 
        System.out.println("Elevator " + id + "moving " + direction);
        System.out.println("Elevator " + id + "stops at floor " + floorId);

        door.open();
        door.close();
    }

    public int getCuurentFloor()
    {
        return currentFloor;
    }
}

public class ElevatorController {

    private int id;
    private ElevatorCar elevatorCar;

    public ElevatorController(int id)
    {
        this.id= id;
        elevatorCar= new ElevatorCar(id);
    }

    public void acceptRequest(int floor, Direction dir)
    {
        ElevatorSystem.elevatorControlStrategy.getPendingRequestList().add(new PendingRequests(floor, dir));

        controlCar();
    }
    private void controlCar()
    {

        ElevatorSystem.elevatorControlStrategy.moveElevator(this);
        System.out.println("Elevator moving...");
    }


}


public class ElevatorSystem {
    private List<ElevatorController> elevatorControllerList= new ArrayList<ElevatorController>();
    public static ElevatorControlStrategy elevatorControlStrategy;
    public static ElevatorSelectionStrategy elevatorSelectionStrategy;
    public List<Floor> floors= new ArrayList<Floor>();

    public static ElevatorSystem INSTANCE= new ElevatorSystem();

    private ElevatorSystem()
    {

    }

    public void addElevator(ElevatorController e)
    {
        elevatorControllerList.add(e);
    }
    public void removeElevator(ElevatorController e)
    {
        elevatorControllerList.remove(e);
    }
    public void setElevatorControlStrategy(ElevatorControlStrategy elevatorControlStrategy)
    {
        this.elevatorControlStrategy= elevatorControlStrategy;
    }
    public void setElevatorSelectionStrategy(ElevatorSelectionStrategy elevatorSelectionStrategy)
    {
        this.elevatorSelectionStrategy= elevatorSelectionStrategy;
    }
    public  void addFloor(Floor floor)
    {
        floors.add(floor);
    }

}


public class InternalDispatcher {

    public  void submitRequest(int floor, Direction dir, int elevatorId)
    {
        for(ElevatorController eController: ElevatorSystem.INSTANCE.getElevatorControllerList())
        {
            if(eController.getId()== elevatorId)
            {
                eController.acceptRequest(floor, dir);
            }
        }
    }
}

public class ExternalDispatcher {

    public static ExternalDispatcher INSTANCE = new ExternalDispatcher();
    private ExternalDispatcher()
    {

    }

    public  void submitRequest(int floor, Direction dir)
    {
        int elevatorId= ElevatorSystem.elevatorSelectionStrategy.selectElevator(floor, dir);
        System.out.println("Selected elevator " + elevatorId);
        for(ElevatorController eController: ElevatorSystem.INSTANCE.getElevatorControllerList())
        {
            if(eController.getId()== elevatorId)
            {
                eController.acceptRequest(floor, dir);
            }
        }
    }
}

