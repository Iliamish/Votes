package com.alva.vibory.Classes;

import com.alva.vibory.Models.Item;

public class Globals {
    private static Globals instance;
    private Globals() {}
    private int checkcand = -1;
    private int prevcand = -1;
    private int total = 1;

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return Globals.instance;
    }

   private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCheckcand() {
        return checkcand;
    }

    public void setCheckcand(int checkcand) {
        this.checkcand = checkcand;
    }

    public int getPrevcand() {
        return prevcand;
    }

    public void setPrevcand(int prevcand) {
        this.prevcand = prevcand;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

