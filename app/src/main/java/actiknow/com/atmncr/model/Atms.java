package actiknow.com.atmncr.model;

import android.util.Log;

public class Atms {
    private int atm_id;
    private String atm_name, atm_bank, atm_location, atm_image;

    public Atms () {
    }

    public Atms (int atm_id, String atm_name, String atm_bank, String atm_location, String atm_image) {
        this.atm_id = atm_id;
        this.atm_name = atm_name;
        this.atm_bank = atm_bank;
        this.atm_location = atm_location;
        this.atm_image = atm_image;
    }

    public int getAtm_id() {
        return atm_id;
    }

    public void setAtm_id (int atm_id) {
        this.atm_id = atm_id;
        Log.d ("atm_id", "" + atm_id);
    }

    public String getAtm_name () {
        return atm_name;
    }

    public void setAtm_name (String atm_name) {
        this.atm_name = atm_name;
        Log.d ("atm_name", atm_name);
    }

    public String getAtm_bank () {
        return atm_bank;
    }

    public void setAtm_bank (String atm_bank) {
        this.atm_bank = atm_bank;
        Log.d ("atm_bank", atm_bank);
    }

    public String getAtm_location () {
        return atm_location;
    }

    public void setAtm_location (String atm_location) {
        this.atm_location = atm_location;
        Log.d ("atm_location", atm_location);
    }

    public String getAtm_image () {
        return atm_image;
    }

    public void setAtm_image(String atm_image) {
        this.atm_image = atm_image;
        Log.d ("atm_image", atm_image);
    }
}
