package com.shadebyte.monthlycrates.language;

/**
 * The current file has been created by Kiran Hart
 * Date Created: 6/27/2018
 * Time Created: 8:11 PM
 * Usage of any code found within this class is prohibited unless given explicit permission otherwise.
 */
public enum Lang {

    PREFIX("prefix"),
    HELP("help"),

    CREATE_COMMAND("commands.create"),
    REMOVE_COMMAND("commands.remove"),
    EDIT_COMMAND("commands.edit"),
    GIVEALL_COMMAND("commands.giveall"),
    GIVE_COMMAND("commands.give"),

    NO_PERMISSION("nopermission"),
    PLAYERS_ONLY("playersonly"),
    PLAYER_OFFLINE("playeroffline"),
    INVALID_SUBCOMMAND("invalidsubcommand"),
    DISABLED("disabled"),
    EDIT_CANCEL("canceledit"),
    NOT_A_NUMBER("notanumber"),
    LISTING_FOUND("listing.found"),
    LISTING_NONE("listing.none"),

    CRATE_CREATED("crate.created"),
    CRATE_REMOVED("crate.removed"),
    CRATE_SAVED("crate.saved"),
    CRATE_GIVE("crate.give"),
    CRATE_GIVEALL("crate.giveall"),
    CRATE_RECEIVED("crate.received"),
    CRATE_EXIST("crate.exist"),
    CRATE_MISSING("crate.missing"),
    CRATE_EDIT_TITLE("crate.edit.title"),
    CRATE_EDIT_STACK_TITLE("crate.edit.stacktitle"),
    CRATE_CANT_EXIT("crate.cantexit"),
    CRATE_CANT_OPEN("crate.cantopen"),


    ;

    private String node;

    Lang(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
