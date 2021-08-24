package com.automationanywhere.botcommand.samples.commands.utils;

public class CloneClass implements Cloneable {
    int a;
    double b;

    // This method calls Object's clone().
    CloneClass getClone() {
        try {
            // call clone in Object.
            return (CloneClass) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println (" Cloning not allowed. " );
            return this;
        }
    }
}