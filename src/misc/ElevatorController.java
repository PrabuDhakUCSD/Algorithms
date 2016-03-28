package misc;

enum RunningState {
    IDLE,
    MOVING_UP,
    MOVING_DOWN
}

class Elevator {
    private int currentFloor;
    private RunningState state;
    
    public Elevator() {
        currentFloor = 1;
        state = RunningState.IDLE;
    }
    
    int getCurrentFloor() {
        return currentFloor;
    }
    
    void setState(RunningState state) {
        this.state = state;
    }
    
    boolean moveUp() {
        return false;
    }
    
    boolean moveDown() {
        return false;
    }
}

abstract class Button {
    boolean isOn;
    
    void turnOn() {
        isOn = true;
    }
    
    void turnOff() {
        isOn = false;
    }
    
    boolean getStatus() {
        return isOn;
    }
}

class FloorButton extends Button {
    int floorId;
    
    FloorButton(int id) {
        this.floorId = id;
    }
    
    boolean makeRequest(ElevatorController controller) {
        return controller.makeRequest(floorId);
    }
}

class ElevatorButton extends Button {
    boolean makeRequest(ElevatorController controller, int targetFloorId) {
        return controller.makeRequest(targetFloorId);
    }
}

enum Direction {
    UP,
    DOWN
}

class Move {
    int targetFloor;
    Direction dir;
}

class RequestManager {
    private final int TOTAL_FLOORS;
    boolean[] requests;
    
    RequestManager(int totalFloors) {
        TOTAL_FLOORS = totalFloors;
        requests = new boolean[TOTAL_FLOORS];
    }
    
    void addRequest(int floorId) {
        requests[floorId] = true;
    }
    
    void removeRequest(int floorId) {
        requests[floorId] = false;
    }
}

interface IElevatorLogic {
    public Move getNextMove(boolean[] requests, int currentFloor,
            RunningState runState);
}

class ClosestElevatorLogic implements IElevatorLogic {
    public ClosestElevatorLogic() {
    }
    
    @Override
    public Move getNextMove(boolean[] requests, int currentFloor,
            RunningState runState) {
        return null;
    }
}

public class ElevatorController {
    private final int totalFloors;
    Elevator elevator;
    RequestManager requestManager;
    IElevatorLogic elevatorLogic;
    ElevatorButton elevatorButton;
    FloorButton[] floorButtons;
    
    ElevatorController(int totalFloors) {
        this.totalFloors = totalFloors;
        elevator = new Elevator();
        requestManager = new RequestManager(totalFloors);
        elevatorLogic = new ClosestElevatorLogic();
        elevatorButton = new ElevatorButton();
        floorButtons = new FloorButton[totalFloors];
    }
    
    boolean makeRequest(int floorId) {
        return true;
    }
}