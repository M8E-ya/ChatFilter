package com.gmail.perpltxed.chatfilter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ChatFilter extends JavaPlugin implements Listener {

    private Map<String, String> wordReplacements;

    @Override
    public void onEnable() {
        // Create and load the configuration file
        saveDefaultConfig();
        reloadWordReplacements();

        // Register the chat listener
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        // Filter the chat message
        String filteredMessage = filterMessage(message);

        // Set the filtered message
        event.setMessage(filteredMessage);
    }

    private String filterMessage(String message) {
        for (Map.Entry<String, String> entry : wordReplacements.entrySet()) {
            String filteredWord = entry.getKey();
            String replacement = entry.getValue();

            message = message.replaceAll("(?i)\\b" + filteredWord + "\\b", replacement);
        }

        return message;
    }

    private void reloadWordReplacements() {
        // Clear the existing word replacements
        wordReplacements = new HashMap<>();

        // Load the word replacements from the configuration file
        FileConfiguration config = getConfig();
        ConfigurationSection replacementsSection = config.getConfigurationSection("word-replacements");
        if (replacementsSection != null) {
            for (String key : replacementsSection.getKeys(false)) {
                String filteredWord = key;
                String replacement = replacementsSection.getString(key);

                wordReplacements.put(filteredWord, replacement);
            }
        }
    }
}

