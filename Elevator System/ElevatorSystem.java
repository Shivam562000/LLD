import java.util.*;

// Observer interface for observing elevator movements
interface ElevatorObserver {
    void update(int elevatorId, int currentFloor);
}

// Request class representing a request made by a person
class Request {
    private int currentFloor;
    private int desiredFloor;
    private Direction direction;
    private Location location;

    public Request(int currentFloor, int desiredFloor, Direction direction, Location location) {
        this.currentFloor = currentFloor;
        this.desiredFloor = desiredFloor;
        this.direction = direction;
        this.location = location;
    }

    public int getDesiredFloor() {
        return desiredFloor;
    }

    public Direction getDirection() {
        return direction;
    }
}

// Enumeration for elevator direction
enum Direction {
    UP,
    DOWN,
    IDLE
}

// Enumeration for request location
enum Location {
    INSIDE_ELEVATOR,
    OUTSIDE_ELEVATOR
}

// Elevator class representing an elevator in the system
class Elevator {
    private int id;
    private int currentFloor;
    private PriorityQueue<Request> upQueue;
    private PriorityQueue<Request> downQueue;
    private Direction direction;
    private List<ElevatorObserver> observers;

    public Elevator(int id, int startingFloor) {
        this.id = id;
        this.currentFloor = startingFloor;
        this.upQueue = new PriorityQueue<>(Comparator.comparingInt(Request::getDesiredFloor));
        this.downQueue = new PriorityQueue<>((a, b) -> b.getDesiredFloor() - a.getDesiredFloor());
        this.direction = Direction.IDLE;
        this.observers = new ArrayList<>();
    }

    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (ElevatorObserver observer : observers) {
            observer.update(id, currentFloor);
        }
    }

    public void sendRequest(Request request) {
        if (request.getDirection() == Direction.UP) {
            upQueue.offer(request);
        } else {
            downQueue.offer(request);
        }
        if (direction == Direction.IDLE) {
            direction = request.getDirection();
            move();
        }
    }

    private void move() {
        PriorityQueue<Request> queue = direction == Direction.UP ? upQueue : downQueue;
        while (!queue.isEmpty()) {
            Request request = queue.poll();
            currentFloor = request.getDesiredFloor();
            notifyObservers();
        }
        direction = Direction.IDLE;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
}

// ElevatorController class responsible for managing elevators
class ElevatorController implements ElevatorObserver {
    private List<Elevator> elevators;

    public ElevatorController(int numElevators, int startingFloor) {
        elevators = new ArrayList<>();
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i + 1, startingFloor);
            elevator.addObserver(this); // Add ElevatorController as an observer to each elevator
            elevators.add(elevator);
        }
    }

    // Implement the update method to observe elevator movements
    public void update(int elevatorId, int currentFloor) {
        System.out.println("Elevator " + elevatorId + " reached floor " + currentFloor);
    }

    public void requestElevator(int floor, Direction direction, Location location) {
        Elevator selectedElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - floor);
            if (distance < minDistance) {
                minDistance = distance;
                selectedElevator = elevator;
            }
        }

        if (selectedElevator != null) {
            selectedElevator.sendRequest(new Request(floor, floor, direction, location));
        }
    }
}

// Factory class for creating instances of ElevatorController
class ElevatorControllerFactory {
    public static ElevatorController createElevatorController(int numElevators, int startingFloor) {
        return new ElevatorController(numElevators, startingFloor);
    }
}

// Main class for testing the elevator system
public class ElevatorSystem {
    public static void main(String[] args) {
        ElevatorController controller = ElevatorControllerFactory.createElevatorController(2, 0);

        // Example usage
        controller.requestElevator(3, Direction.UP, Location.INSIDE_ELEVATOR);
        controller.requestElevator(5, Direction.UP, Location.OUTSIDE_ELEVATOR);
        controller.requestElevator(4, Direction.UP, Location.OUTSIDE_ELEVATOR);
        controller.requestElevator(5, Direction.DOWN, Location.INSIDE_ELEVATOR);
        controller.requestElevator(1, Direction.DOWN, Location.INSIDE_ELEVATOR);
        controller.requestElevator(2, Direction.DOWN, Location.OUTSIDE_ELEVATOR);
    }
}
