package dev.lightclient.command.impl;

import dev.lightclient.account.OfflineAccount;
import dev.lightclient.command.Command;
import dev.lightclient.gui.AccountScreen;
import dev.lightclient.manager.AccountManager;

/** Manage offline accounts from chat: add, remove, login, list. */
public final class AccountCommand extends Command {
    public AccountCommand() {
        super("account", "Manage offline accounts", "account add|remove|login|list <name>", "acc");
    }

    @Override
    public void execute(String[] args) {
        AccountManager manager = client().getAccountManager();
        if (args.length == 0) {
            mc.setScreen(new AccountScreen());
            return;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "list" -> {
                if (manager.getAccounts().isEmpty()) {
                    send("§7No offline accounts stored.");
                    return;
                }
                send("§dOffline accounts:");
                for (OfflineAccount account : manager.getAccounts()) {
                    send(" §8- §f" + account.getUsername());
                }
            }
            case "add" -> {
                if (args.length < 2) {
                    error("Usage: .account add <name>");
                    return;
                }
                if (manager.add(args[1])) {
                    send("§aAdded account §f" + args[1]);
                } else {
                    error("Invalid or duplicate username.");
                }
            }
            case "remove", "del", "delete" -> {
                if (args.length < 2) {
                    error("Usage: .account remove <name>");
                    return;
                }
                OfflineAccount target = find(manager, args[1]);
                if (target == null) {
                    error("No such account.");
                    return;
                }
                manager.remove(target);
                send("§aRemoved account §f" + args[1]);
            }
            case "login", "switch" -> {
                if (args.length < 2) {
                    error("Usage: .account login <name>");
                    return;
                }
                OfflineAccount target = find(manager, args[1]);
                if (target == null) {
                    error("No such account. Add it first with .account add " + args[1]);
                    return;
                }
                if (manager.login(target)) {
                    send("§aLogged in as §f" + target.getUsername());
                } else {
                    error("Failed to switch session.");
                }
            }
            default -> error("Usage: " + getUsage());
        }
    }

    private OfflineAccount find(AccountManager manager, String name) {
        for (OfflineAccount account : manager.getAccounts()) {
            if (account.getUsername().equalsIgnoreCase(name)) {
                return account;
            }
        }
        return null;
    }
}
