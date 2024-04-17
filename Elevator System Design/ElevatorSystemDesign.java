import java.util.*;

// Enum for Direction
enum Direction {
    UP, DOWN, NONE;
}

// Button class
class Button {
    // for External Button
    public void pressButton(int floor, Direction dir){}

    // for Internal Button
    public void pressButton(int floor, Direction dir, int elevatorId){}
}

// Display class
class Display {
    private int floor;
    private Direction dir;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }
}

// Door class
class Door {
    public void open(int id) {
        System.out.println("Door opens for elevator " + id);
    }

    public void close(int id) {
        System.out.println("Door closes for elevator " + id);
    }
}

// ElevatorCar class
class ElevatorCar {
    private int id;
    public Door door;
    private Display display;
    private Button button;
    private int currentFloor;
    private Direction dir;

    public ElevatorCar(int id) {
        this.id = id;
        door = new Door();
        display = new Display();
        currentFloor = 0;
        dir = Direction.NONE;
        button = new InternalButton();
    }

    public void move(Direction dir, int floor) {
        System.out.println("Elevator " + id + " moving " + dir);
        System.out.println("Elevator " + id + " stops at floor " + floor);
        door.open(id);
        door.close(id);
        setDisplay();
    }

    public void pressButton(int floor) {
        Direction dir = Direction.NONE;
        if (floor > currentFloor)
            dir = Direction.UP;
        else if (floor < currentFloor)
            dir = Direction.DOWN;
        button.pressButton(floor, dir, id);
    }

    private void setDisplay() {
        display.setFloor(currentFloor);
        display.setDir(dir);
    }

    public int getCurrentFloor()
    {
        return currentFloor;
    }

    public Direction getDir()
    {
        return dir;
    }
}

// ElevatorController class
class ElevatorController {
    private int id;
    private ElevatorCar elevatorCar;

    public ElevatorController(int id, ElevatorCar elevatorCar) {
        this.id = id;
        this.elevatorCar = elevatorCar;
    }

    

    public void acceptRequest(int floor, Direction dir) {
        System.out.println("Elevator " + id + " received request from floor " + floor + " for direction " + dir);
        int currentFloor = elevatorCar.getCurrentFloor();
        Direction currentDirection = elevatorCar.getDir();
    
        // Determine action based on current state and request
        if (currentFloor == floor) {
            // Elevator is already at the requested floor, open the door immediately
            elevatorCar.door.open(id);
            elevatorCar.door.close(id);
        } else if (currentDirection == Direction.NONE) {
            // Elevator is idle, start it and move towards the requested floor
            if (floor > currentFloor) {
                elevatorCar.move(Direction.UP, floor);
            } else {
                elevatorCar.move(Direction.DOWN, floor);
            }
        } else if (currentDirection == Direction.UP && floor > currentFloor) {
            // Elevator is moving up and request is in the same direction, continue
            // serving the request on the way
            elevatorCar.move(Direction.UP, floor);
        } else if (currentDirection == Direction.DOWN && floor < currentFloor) {
            // Elevator is moving down and request is in the same direction, continue
            // serving the request on the way
            elevatorCar.move(Direction.DOWN, floor);
        } else {
            // Elevator is moving in the opposite direction, change its direction
            // and serve the request after reaching the current destination
            // For simplicity, we'll ignore intermediate stops and directly move to the requested floor
            if (currentDirection == Direction.UP) {
                elevatorCar.move(Direction.DOWN, floor);
            } else {
                elevatorCar.move(Direction.UP, floor);
            }
        }
    }

    public int getId() {
        return id;
    }
    
}

// ElevatorSystem class
class ElevatorSystem {
    private List<ElevatorController> elevatorControllerList = new ArrayList<>();
    private static ElevatorSystem INSTANCE = new ElevatorSystem();

    private ElevatorSystem() {}

    public static ElevatorSystem getInstance() {
        return INSTANCE;
    }

    public void addElevator(ElevatorController e) {
        elevatorControllerList.add(e);
    }

    public void removeElevator(ElevatorController e) {
        elevatorControllerList.remove(e);
    }

    public void addFloor(int id) {
        // Functionality to add floor
    }

    public List<ElevatorController> getElevatorControllerList() {
        return elevatorControllerList;
    }
}

// InternalButton class
class InternalButton extends Button {
    private InternalDispatcher idispatcher;
    private List<Integer> floors = new ArrayList<>();

    public InternalButton() {
        idispatcher = new InternalDispatcher();
    }

    public void pressButton(int floor, Direction dir, int elevatorId) {
        floors.add(floor);
        System.out.println("Pressed floor " + floor + " from elevator " + elevatorId);
        idispatcher.submitRequest(floor, dir, elevatorId);
    }

    @Override
    public void pressButton(int floor, Direction dir) {
        // Not used for internal button
    }
}

// ExternalButton class
class ExternalButton extends Button {
    private ExternalDispatcher edispatcher = ExternalDispatcher.getInstance();
    private Direction direction;

    public void pressButton(int floor, Direction dir) {
        direction = dir;
        System.out.println("Pressed " + direction + " from floor " + floor);
        edispatcher.submitRequest(floor, dir);
    }

    @Override
    public void pressButton(int floor, Direction dir, int elevatorId) {
        // Not used for external button
    }
}

// Floor class
class Floor {
    private int id;
    private Button button;

    public Floor(int id) {
        this.id = id;
        button = new ExternalButton();
    }

    public void pressButton(Direction dir) {
        button.pressButton(id, dir);
    }
}

// InternalDispatcher class
class InternalDispatcher {
    public void submitRequest(int floor, Direction dir, int elevatorId) {
        for (ElevatorController eController : ElevatorSystem.getInstance().getElevatorControllerList()) {
            if (eController.getId() == elevatorId) {
                eController.acceptRequest(floor, dir);
            }
        }
    }
}

// ExternalDispatcher class
class ExternalDispatcher {
    private static ExternalDispatcher INSTANCE = new ExternalDispatcher();

    private ExternalDispatcher() {}

    public static ExternalDispatcher getInstance() {
        return INSTANCE;
    }

    public void submitRequest(int floor, Direction dir) {
        // Logic to select elevator and assign request
        // Here we can just print for simulation
        System.out.println("External Dispatcher received request from floor " + floor + " for direction " + dir);
    }


    public void pressButton(int floor, Direction dir) {
        // Not used for external dispatcher
    }
}

// PendingRequests class
class PendingRequests {
    private int floor;
    private Direction dir;

    public PendingRequests(int floor, Direction dir) {
        this.floor = floor;
        this.dir = dir;
    }
}

// ElevatorControlStrategy class
abstract class ElevatorControlStrategy {
    protected Queue<PendingRequests> pendingRequestList = new LinkedList<>();
    protected List<ElevatorController> elevatorControllerList = ElevatorSystem.getInstance().getElevatorControllerList();

    public abstract void moveElevator(ElevatorController elevatorController);
}

// LookAlgorithm class
class LookAlgorithm extends ElevatorControlStrategy {
    @Override
    public void moveElevator(ElevatorController elevatorController) {
        // Implement the Look Algorithm here
        // This algorithm moves the elevator to the nearest floor in the requested direction
        System.out.println("Look Algo");
    }
}

// Main class
public class ElevatorSystemDesign {
    public static void main(String[] args) {
        // Create elevator system
        ElevatorSystem elevatorSystem = ElevatorSystem.getInstance();

        // Create Elevator cars and controllers
        ElevatorCar car1 = new ElevatorCar(1);
        ElevatorController controller1 = new ElevatorController(1, car1);
        elevatorSystem.addElevator(controller1);

        ElevatorCar car2 = new ElevatorCar(2);
        ElevatorController controller2 = new ElevatorController(2, car2);
        elevatorSystem.addElevator(controller2);

        // Test cases
        Floor floor1 = new Floor(1);
        Floor floor2 = new Floor(2);
        Floor floor3 = new Floor(3);

        // Simulating button presses
        floor1.pressButton(Direction.UP);
        floor2.pressButton(Direction.DOWN);
        floor3.pressButton(Direction.UP);

        // Internal button press (from inside the elevator)
        car1.pressButton(5);
        car2.pressButton(10);

        // Additional edge test cases
        Floor floor4 = new Floor(4);
        floor4.pressButton(Direction.NONE);  // Should not trigger anything

        car1.pressButton(-1);  // Invalid floor
        car2.pressButton(20);  // Invalid floor
    }
}
