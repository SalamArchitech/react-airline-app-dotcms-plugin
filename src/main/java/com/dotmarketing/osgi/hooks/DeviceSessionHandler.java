package com.dotmarketing.osgi.hooks;

import com.dotcms.repackage.org.directwebremoting.guice.ApplicationScoped;
import com.google.gson.JsonObject;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DeviceSessionHandler {

    private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Device> devices = new HashSet<>();

    public void addSession(Session session){
        sessions.add(session);
        for (Device device : devices) {
            JsonObject addMessage = createAddMessage(device);
            sendToSession(session, "added devices");
        }

    }

    public void removeSession(Session session ){
        sessions.remove(session);
    }

    public List<Device> getDevices() {
        return new ArrayList<>(devices);
    }

    public void addDevice(Device device) {
        device.setID(deviceId);
        devices.add(device);
        deviceId++;
        sendToAllConnectedSessions("Added a device");
    }

    public void removeDevice(int id) {
        Device device = getDeviceById(id);
        sendToAllConnectedSessions("Device removed");
    }

    public void toggleDevice(int id) {
    }

    private Device getDeviceById(int id) {
        return null;
    }

    private JsonObject createAddMessage(Device device) {
        return null;
    }

    private void sendToAllConnectedSessions(String message) {
    }

    private void sendToSession(Session session, String message) {

    }
}
