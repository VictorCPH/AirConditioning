package CentralAirConditioning;

import java.util.LinkedList;


public class RoomList {
	private LinkedList<RoomInfo> rooms;
	
	public RoomList() {
		rooms = new LinkedList<RoomInfo>();
	}
	
	
	public LinkedList<RoomInfo> getRooms() {
		return rooms;
	}


	public void setRooms(LinkedList<RoomInfo> rooms) {
		this.rooms = rooms;
	}


	public RoomInfo getOneRoom(int roomNumber) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber)
				return roomInfo;
		}
		return null;
	}
	
	public void removeOneRoom(int roomNumber) {
		int i = 0;
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber) {
				rooms.remove(i);
				break;
			}
			i++;
		} 
	}
	
	public void addOneRoom(int roomNumber) {
		RoomInfo roomInfo = new RoomInfo(roomNumber);
		rooms.add(roomInfo);
	}
	
	public double getOneRoomCurTemperature(int roomNumber) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber)
				return roomInfo.getTemperature();
		}
		return 0.0;
	}
	
	public void setOneRoomCurTemperature(int roomNumber, double temperature) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber) {
				roomInfo.setTemperature(temperature);
				break;
			}
		}
	}

	public void setOneRoomState(int roomNumber, State state) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber) {
				roomInfo.setMode(state);
				break;
			}
		}
	}
	
	public void setOneRoomWind(int roomNumber, Wind wind) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber) {
				roomInfo.setWind(wind);
				break;
			}
		}
	}
	
	public Wind getOneRoomWind(int roomNumber) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber)
				return roomInfo.getWind();
		}
		return null;
	}
	
	public State getOneRoomState(int roomNumber) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber)
				return roomInfo.getState();
		}
		return null;
	}
	
	@Override
	public synchronized String toString() {
		return "RoomList [rooms=" + rooms + "]";
	}
	
	public void updateOneRoomConsumption(int roomNumber, double consumption) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber) {
				roomInfo.setConsumption(consumption);
				break;
			}
		}
	}
	
	public void updateOneRoomFee(int roomNumber, double fee) {
		for (RoomInfo roomInfo : rooms) {
			if (roomInfo.getRoomNumber() == roomNumber) {
				roomInfo.setFee(fee);
				break;
			}
		}
	}
	
}
