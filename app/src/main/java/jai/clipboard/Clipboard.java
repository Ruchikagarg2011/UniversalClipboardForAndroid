package jai.clipboard;

public class Clipboard {

    //history/userid/{json}
    String deviceName;
    String copyContent;
    String messageType;

    public Clipboard(String deviceName, String copyContent, String messageType) {
        this.deviceName = deviceName;
        this.copyContent = copyContent;
        this.messageType = messageType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCopyContent() {
        return copyContent;
    }

    public void setCopyContent(String copyContent) {
        this.copyContent = copyContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }


}
