package com.shadebyte.monthlycrates;

import com.massivestats.MassiveStats;
import com.shadebyte.monthlycrates.cmd.CommandManager;
import com.shadebyte.monthlycrates.crate.CratePane;
import com.shadebyte.monthlycrates.language.Locale;
import com.shadebyte.monthlycrates.listeners.CrateEditListeners;
import com.shadebyte.monthlycrates.listeners.MGUIListener;
import com.shadebyte.monthlycrates.listeners.PlayerListeners;
import com.shadebyte.monthlycrates.utils.ConfigWrapper;
import com.shadebyte.monthlycrates.utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class Core extends JavaPlugin {

    //Storage
    public Map<UUID, String> editingCrate;
    public Map<UUID, CratePane> editingCrateItems;
    public List<UUID> editingTitle;
    public List<UUID> editingStack;
    public List<UUID> openingCrate;

    //Instance variable.
    private static Core instance;

    //Configuration Files
    private static ConfigWrapper crates;
    private static ConfigWrapper users_opening;

    //Locale
    private Locale locale;

    //Settings
    private Settings settings = null;

    //Command Manager
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Initialize the instance value to this class
        instance = this;

        editingCrate = new HashMap<>();
        editingCrateItems = new HashMap<>();
        editingTitle = new ArrayList<>();
        editingStack = new ArrayList<>();
        openingCrate = new ArrayList<>();

        initFiles();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        //Locales
        Locale.init(this);
        Locale.saveDefaultLocale("en_US");
        this.locale = Locale.getLocale(this.getConfig().getString("Locale", "en_US"));

        initEvents();

        settings = new Settings();
        commandManager = new CommandManager();

        commandManager.initialize();

        try {
            MassiveStats stats = new MassiveStats(this);
            stats.setListenerDisabled(false);
        } catch (Exception e) {
            Debugger.report(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //Remove initialization from instance
        instance = null;
    }

    private void initFiles() {
        crates = new ConfigWrapper(this, "", "crates.yml");
        users_opening = new ConfigWrapper(this,"","users_opening.yml");
        if (!new File(this.getDataFolder(), "crates.yml").exists())
            crates.getConfig().createSection("crates");
        if (!new File(this.getDataFolder(), "users_opening.yml").exists())
            users_opening.getConfig().createSection("player-uuids");
        crates.saveConfig();
        users_opening.saveConfig();
    }

    private void initEvents() {
        PluginManager p = Bukkit.getPluginManager();
        p.registerEvents(new MGUIListener(), this);
        p.registerEvents(new CrateEditListeners(), this);
        p.registerEvents(new PlayerListeners(), this);
    }

    /**
     * @return the instance value
     */
    public static Core getInstance() {
        return instance;
    }

    /**
     * @return the crates file.
     */
    public static ConfigWrapper getCrates() {
        return crates;
    }

    public static ConfigWrapper getUsersOpening() {
        return users_opening;
    }

    /**
     * @return the Locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @return command manager
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }
}
