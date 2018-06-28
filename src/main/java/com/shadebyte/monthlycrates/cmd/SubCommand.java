package com.shadebyte.monthlycrates.cmd;

import org.bukkit.command.CommandSender;

/**
 * The current file has been created by Kiran Hart
 * Date Created: 6/27/2018
 * Time Created: 7:43 PM
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise.
 */
public abstract class SubCommand {

    /*
    /command <sub command>  args[++]
     */

    public SubCommand() {}

    public abstract void onCommand(CommandSender sender, String[] args);

    public abstract String name();

    public abstract String info();

    public abstract String[] aliases();
}
